package at.ac.tuwien.rep.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Report {
	private final long contentLength;
	private final InputStream reportStream;
	
	public Report(long contentLength, InputStream reportStream) {
		this.contentLength = contentLength;
		this.reportStream = reportStream;
	}
	
	public Report(ByteArrayOutputStream outStream) {
		this.contentLength = outStream.size();
		this.reportStream = new ByteArrayInputStream(outStream.toByteArray());
	}

	public long getContentLength() {
		return contentLength;
	}

	public InputStream getReportStream() {
		return reportStream;
	}
}
