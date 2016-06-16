package at.ac.tuwien.rep.report;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.group.Groups;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import at.ac.tuwien.rep.model.ResourceNomination;

public class ReportGenerator {
	public static Report createReport(String type, List<ResourceNomination> nominations) throws DRException {
		DRDataSource dataSource = new DRDataSource("resource", "region", "quantity");
		nominations.forEach(n -> dataSource.add(n.getResource(), n.getRegion().getName(), n.getQuantity()));
		StyleBuilder boldStyle = stl.style().bold(); 
		StyleBuilder boldCenteredStyle = stl.style(boldStyle)
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle)
				.setBorder(stl.pen1Point())
				.setBackgroundColor(Color.LIGHT_GRAY);		
		TextColumnBuilder<String> regionColumn = col.column("Region", "region", String.class);
		TextColumnBuilder<String> resourceColumn = col.column("Resource", "resource", String.class);
		TextColumnBuilder<BigDecimal> quantityColumn = col.column("Quantity", "quantity", BigDecimal.class);
		ByteArrayOutputStream reportStream = new ByteArrayOutputStream();
		ColumnGroupBuilder groupByClause = Groups.group(regionColumn);
		report()
		.setColumnTitleStyle(columnTitleStyle) 
		.setColumnStyle(stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
		.highlightDetailEvenRows()
		.columns(regionColumn, resourceColumn, quantityColumn)
		.groupBy(groupByClause)
		.title(cmp.text("Resources per region").setStyle(boldCenteredStyle))
		.pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
		.setDataSource(dataSource)
		.toPdf(reportStream);
		return new Report(reportStream);
	}
}
