
---------------------------
什么场景该用哪个方法
---------------------------

    - 导出
	    1.正规excel导出 (格式简单,数据量可以,5W以内吧)
	        注解方式:  ExcelExportUtil.exportExcel(ExportParams entity, Class<?> pojoClass,Collection<?> dataSet) 
	    2.不定多少列,但是格式依然简单数据库不大
	        自定义方式: ExcelExportUtil.exportExcel(ExportParams entity, List<ExcelExportEntity> entityList,Collection<?> dataSet)
	    3.数据量大超过5W,还在100W以内
	        注解方式 ExcelExportUtil.exportBigExcel(ExportParams entity, Class<?> pojoClass,IExcelExportServer server, Object queryParams)
	        自定义方式: ExcelExportUtil.exportBigExcel(ExportParams entity, List<ExcelExportEntity> excelParams,IExcelExportServer server, Object queryParams)
	    4.样式复杂,数据量尽量别大
	        模板导出 ExcelExportUtil.exportExcel(TemplateExportParams params, Map<String, Object> map)
	    5.一次导出多个风格不一致的sheet
	        模板导出 ExcelExportUtil.exportExcel(Map<Integer, Map<String, Object>> map,TemplateExportParams params) 
	    6.一个模板但是要导出非常多份
	        模板导出 ExcelExportUtil.exportExcelClone(Map<Integer, List<Map<String, Object>>> map,TemplateExportParams params)
	    7.模板无法满足你的自定义,试试html
	        自己构造html,然后我给你转成excel  ExcelXorHtmlUtil.htmlToExcel(String html, ExcelType type)
	    8.数据量过百万级了.放弃excel吧,csv导出
	        注解方式: CsvExportUtil.exportCsv(CsvExportParams params, Class<?> pojoClass, OutputStream outputStream)
	        自定义方式: CsvExportUtil.exportCsv(CsvExportParams params, List<ExcelExportEntity> entityList, OutputStream outputStream)
        9.word导出
            模板导出: WordExportUtil.exportWord07(String url, Map<String, Object> map)
        10.PDF导出
            模板导出: TODO 
    - 导入 
        如果想提高性能 ImportParams 的concurrentTask 可以帮助并发导入,仅单行,最小1000
        excel有单个的那种特殊读取,readSingleCell 参数可以支持
        1. 不需要检验,数据量不大(5W以内)
            注解或者MAP: ExcelImportUtil.importExcel(File file, Class<?> pojoClass, ImportParams params)
        2. 需要导入,数据量不大
            注解或者MAP: ExcelImportUtil.importExcelMore(InputStream inputstream, Class<?> pojoClass, ImportParams params)
        3. 数据量大了,或者你有特别多的导入操作,内存比较少,仅支持单行
           SAX方式  ExcelImportUtil.importExcelBySax(InputStream inputstream, Class<?> pojoClass, ImportParams params, IReadHandler handler)
        4. 数据量超过EXCEL限制,CSV读取
            小数据量: CsvImportUtil.importCsv(InputStream inputstream, Class<?> pojoClass,CsvImportParams params)
            大数据量: CsvImportUtil.importCsv(InputStream inputstream, Class<?> pojoClass,CsvImportParams params, IReadHandler readHandler)


	
---------------------------
关于Excel导出XLS和XLSX区别
---------------------------

	1.导出时间XLS比XLSX快2-3倍
	2.导出大小XLS是XLSX的2-3倍或者更多
	3.导出需要综合网速和本地速度做考虑^~^
--------------------------
maven
--------------------------
maven库应该都可以了
SNAPSHOT 版本-很少发布
https://oss.sonatype.org/content/groups/public/
```xml
		 <dependency>
              <groupId>cn.afterturn</groupId>
              <artifactId>easypoi-spring-boot-starter</artifactId>
              <version>4.4.0</version>
         </dependency>
        <!-- 建议只用start -->
		 <dependency>
			<groupId>cn.afterturn</groupId>
			<artifactId>easypoi-base</artifactId>
			<version>4.4.0</version>
		</dependency>
		<dependency>
			<groupId>cn.afterturn</groupId>
			<artifactId>easypoi-web</artifactId>
			<version>4.4.0</version>
		</dependency>
		<dependency>
			<groupId>cn.afterturn</groupId>
			<artifactId>easypoi-annotation</artifactId>
			<version>4.4.0</version>
		</dependency>
		
```


--------------------------
pom说明
--------------------------
word和sax读取的时候才使用,就不是必须的了,请手动引用,JSR303的校验也是可选的,PDF的jar也是可选的
```xml
			<!-- sax 读取时候用到的 -->
			<dependency>
				<groupId>xerces</groupId>
				<artifactId>xercesImpl</artifactId>
				<version>${xerces.version}</version>
				<optional>true</optional>
			</dependency>
			<dependency>
				<groupId>org.apache.poi</groupId>
				<artifactId>poi-scratchpad</artifactId>
				<version>${poi.version}</version>
				<optional>true</optional>
			</dependency>
			
			<!-- Word 需要使用 -->
            <dependency>
                <groupId>org.apache.poi</groupId>
                <artifactId>ooxml-schemas</artifactId>
                <version>1.3</version>
                <optional>true</optional>
            </dependency>
			
			<!-- 校验,下面两个实现 -->
			<dependency>
				<groupId>org.hibernate</groupId>
				<artifactId>hibernate-validator</artifactId>
				<version>5.1.3.Final</version>
				<optional>true</optional>
			</dependency>
			
			<dependency>
				<groupId>org.apache.bval</groupId>
				<artifactId>org.apache.bval.bundle</artifactId>
				<version>1.1.0</version>
			</dependency>
			
			<!-- PDF -->
			<dependency>
				<groupId>com.itextpdf</groupId>
				<artifactId>itextpdf</artifactId>
				<version>5.5.6</version>
				<optional>true</optional>
			</dependency>

			<dependency>
				<groupId>com.itextpdf</groupId>
				<artifactId>itext-asian</artifactId>
				<version>5.2.0</version>
				<optional>true</optional>
			</dependency>
```
