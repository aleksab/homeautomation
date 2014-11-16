package no.home.automation.rfxcom;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RFXComSerialConnector implements RFXComConnectorInterface
{
	private static final Logger					logger			= LoggerFactory.getLogger("stdoutLogger");

	private static List<RFXComEventListener>	_listeners		= new ArrayList<RFXComEventListener>();

	InputStream									in				= null;
	OutputStream								out				= null;
	SerialPort									serialPort		= null;
	Thread										readerThread	= null;

	public RFXComSerialConnector()
	{
		logger.debug("Start");
	}

	public void connect(String device) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException
	{
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(device);

		CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

		serialPort = (SerialPort) commPort;
		serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		serialPort.enableReceiveThreshold(1);
		serialPort.disableReceiveTimeout();

		in = serialPort.getInputStream();
		out = serialPort.getOutputStream();

		out.flush();
		if (in.markSupported())
		{
			in.reset();
		}

		readerThread = new SerialReader(in);
		readerThread.start();
	}

	public void disconnect()
	{
		logger.info("Interrupt serial connection");
		readerThread.interrupt();

		logger.info("Close serial stream");
		try
		{
			out.close();
		}
		catch (IOException e)
		{
		}

		// Evert: very frustrating, I cannot get the thread to gracefully shutdown when copying a new jar on a running install.
		// somehow the serialport does not get released...

		// logger.debug("Close serial connection");
		// serialPort.removeEventListener();
		// serialPort.close();

		logger.info("Ready");
	}

	public void sendMessage(byte[] data) throws IOException
	{
		out.write(data);
		out.flush();
	}

	public synchronized void addEventListener(RFXComEventListener rfxComEventListener)
	{
		_listeners.add(rfxComEventListener);
	}

	public synchronized void removeEventListener(RFXComEventListener listener)
	{
		_listeners.remove(listener);
	}

	public class SerialReader extends Thread
	{
		InputStream	in;

		public SerialReader(InputStream in)
		{
			this.in = in;
		}

		public void interrupt()
		{
			super.interrupt();
			try
			{
				in.close();
			}
			catch (IOException e)
			{
			} // quietly close
		}

		public void run()
		{
			logger.info("Started listening");
			final int dataBufferMaxLen = Byte.MAX_VALUE;

			byte[] dataBuffer = new byte[dataBufferMaxLen];

			int msgLen = 0;
			int index = 0;
			boolean start_found = false;

			try
			{

				byte[] tmpData = new byte[20];
				int len = -1;

				while ((len = in.read(tmpData)) > 0)
				{

					byte[] logData = Arrays.copyOf(tmpData, len);
					logger.debug("Received data (len={}): {}", len, DatatypeConverter.printHexBinary(logData));

					for (int i = 0; i < len; i++)
					{

						if (index > dataBufferMaxLen)
						{
							// too many bytes received, try to find new start
							start_found = false;
						}

						if (start_found == false && tmpData[i] > 0)
						{

							start_found = true;
							index = 0;
							dataBuffer[index++] = tmpData[i];
							msgLen = tmpData[i] + 1;

						}
						else if (start_found)
						{

							dataBuffer[index++] = tmpData[i];

							if (index == msgLen)
							{

								// whole message received, send an event

								byte[] msg = new byte[msgLen];

								for (int j = 0; j < msgLen; j++)
									msg[j] = dataBuffer[j];

								RFXComMessageReceivedEvent event = new RFXComMessageReceivedEvent(this);

								try
								{
									Iterator<RFXComEventListener> iterator = _listeners.iterator();

									while (iterator.hasNext())
									{
										((RFXComEventListener) iterator.next()).packetReceived(event, msg);
									}

								}
								catch (Exception e)
								{
									logger.error("Event listener invoking error", e);
								}

								// find new start
								start_found = false;
							}
						}
					}
				}
			}
			catch (InterruptedIOException e)
			{
				Thread.currentThread().interrupt();
				logger.error("Interrupted via InterruptedIOException");
			}
			catch (IOException e)
			{
				logger.error("Reading from serial port failed", e);
			}

			logger.info("Ready reading from serial port");
		}
	}
}