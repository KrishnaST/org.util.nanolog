package org.util.nanolog.internals;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.util.nanolog.LogWriter;
import org.util.nanolog.Logger;

public final class BufferedDailyLogger extends Logger {

	private final Writer writer;

	private final List<String> list = new ArrayList<>();

	public BufferedDailyLogger(LogWriter logWriter) {
		this.writer = logWriter.getWriter();
		list.add("\r\n-----**-----\r\n");
	}

	@Override
	public final void write(String s) {
		if (super.status) list.add(s);
	}

	@Override
	public final void close() {
		super.status = false;
		final StringBuilder sb = new StringBuilder(2000);
		list.forEach(s -> sb.append(s));
		final String s = sb.toString();
		try {
			synchronized (writer) {
				writer.write(s);
				writer.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		list.clear();
	}

}
