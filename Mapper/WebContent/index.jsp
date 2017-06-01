<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<title>ODEK</title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script language="javascript" type="text/javascript">
			function getSchemas(value) {					
				window.location = "newdb?dbname=" + value;
			}
			function submitFTP() {
				var ftpHostName = document.getElementById("txtftpHostName").value;
				var ftpPort = document.getElementById("txtftpPort").value;
				var ftpUser = document.getElementById("txtftpUser").value;
				var ftpPassword = document.getElementById("txtftpPassword").value;
				var ftpRemotedir = document.getElementById("txtftpRemotedir").value;
				window.location = "newftp?ftpHostName=" + ftpHostName+ "&ftpPortNumber=" + ftpPort + "&ftpUser=" + ftpUser+ "&ftpPassword=" + ftpPassword + "&ftpRemotedir="+ ftpRemotedir;
			}
			function submitDB() {				
				var dbHostName = document.getElementById("txtHostName").value;
				var dbPort = document.getElementById("txtPort").value;
				var dbUser = document.getElementById("txtUserName").value;
				var dbPassword = document.getElementById("txtPassword").value;
				var dbname = document.getElementById("databaseType").value;				
				window.location = "newdb?dbHostName=" + dbHostName + "&dbPort="+ dbPort + "&dbUser=" + dbUser + "&dbPassword=" + dbPassword+ "&dbname=" + dbname;
			}
			function getTables(sel) {					
				var schemaName = sel.options[sel.selectedIndex].text;
				window.location = "newdb?schemaId=" + sel.value + "&schemaName="+ schemaName;
			}
			function getTablesFromDB(dbname) {
				window.location = "newdb?database=" + dbname;
			}
			function getTableColumns(sel) {					
				window.location = "newdb?tablename=" + sel;
			}
			function populateTable(csvField) {
				$('#mapTd' + selectedRow).html(csvField);
			}
			var saveAsMapName;
			function setMapName(value){
				saveAsMapName = value;
			}
			function mapFields() {
				var hashtable = "";
				$('#maptable .trclass').each(function() {
					var key, value, staticfield, dataTypefield = null;
					key = $(this).find('.dbfieldclass').html();
					value = $(this).find('.csvfieldclass').html();
					staticfield = $(this).find('.staticfieldclass').html();
					dataTypefield = $(this).find('.dataTypeClass').html();
					if(staticfield == "") staticfield = null;
					hashtable += key + "=" + value + "=" + staticfield + "=" + dataTypefield + "@";
				});
				alert(hashtable);
				window.location = "newdb?csvDBMap=" + hashtable + "&saveAsMapName="+ saveAsMapName;
			}
			var dbOperation;
			function getDBOperation(value){
				dbOperation = value;
				//alert(dbOperation);
				//window.location = "newdb?dbOperation=" + dbOperation;
			}
			function submitForm() {				
				window.location = "newdb?submitform=" + true+ "&dbOperation="+ dbOperation;
			}
			function validate() {
				var staticData = document.getElementById("staticData").value;				
		        if (document.getElementById("staticData").value == "") {
		            alert("Static field may not be blank");
		        }
		        $('#mapStatic' + selectedRow).html(staticData);		        
		    }
			function getmap(sel) {
				window.location = "newdb?mapselected=" + sel;
			}
			function getMapsFromLocation() {
				window.location = "newdb?loadmap="+true ;
			}				
			var selectedRow;
			$(window).load(function() {
				$("#maptable tr").click(function() {
					$(this).addClass('selected').siblings().removeClass('selected');
					selectedRow = $(this).find('td:first').html();
				});				
			});
		</script>
	<style type="text/css">
				.tg {
					border-collapse: collapse;
					border-spacing: 0;
					 border: 1px solid orange;
				}
				.td {
					border: 1px #DDD solid;
					padding: 5px;
					cursor: pointer;
					 border: 1px solid orange;
				}
				.selected {
					background-color: brown;
					color: #ffb366;
				}
				.tg th {
					font-family: Arial, sans-serif;
					font-size: 14px;
					font-weight: normal;
					padding: 10px 5px;
					border-style: solid;
					border-width: 1px;
					overflow: hidden;
					word-break: normal;
					 border: 1px solid orange;
				}
				.tg .tg-qc4u {
					color: #32cb00
				}
				.tg .tg-yw4l {
					vertical-align: top
				}
				.container {
				background-color: Gainsboro;
					display: flex;
					justify-content: center;
					height: auto;
					border: 3px solid #73AD21;
					padding: 100px;
					text-align: center;
					margin: 0 auto;
    				overflow: hidden;
				}
				.db {
					float:right;
					width:100px;
				}				
				.map {
					display: inline-block;
    				margin:0 auto;
    				width:100px;
				}
				.ftp {
					float:left;
    				width:100px;
				}
	</style>
</head>
<body>
	<h1 align="center">Fields Mapper</h1>
	<div class="container">
		<div class="row">
				<div class="container">
					<div id="db">
						
						<div id="dvselectdb" style="float:left">
						<div id="dvHostName">HostName*:<s:textfield id="txtHostName" name="dbHostName" value="localhost"/></div>
						<div id="dvPortNumber">HostPort*:<s:textfield id="txtPort" name="dbPort" value="1433"/></div>
						<div id="dvUserName">UserName*:<s:textfield id="txtUserName" name="dbUser" value="sa"/></div>
						<div id="dvPassword">Password*:<s:password id="txtPassword" name="dbPassword" value="sa2008"/></div>
						<s:select label="Select a Database" headerKey="-1" headerValue="Select database" list="#{'MySQL':'MySQL', 'Microsoft SQL Server':'Microsoft SQL Server', 'Oracle':'Oracle', 'PostgreSQL':'PostgreSQL'}" name="dbname" id="databaseType" /><%--onchange="getSchemas(this.value)" --%>
						<s:submit value="Submit" onclick="submitDB()" />	
							<s:if test="databaseList != null">
								<s:select label="Select a Schema" headerKey="-1" headerValue="Select schema" list="databaseList" name="database" onchange="getTablesFromDB(this.value)" /><br>
							</s:if>
							<s:if test="tables != null">
								<s:select label="Select a Table" headerKey="-1"	headerValue="Select table" list="tables" name="tablename" onchange="getTableColumns(this.value)" />
							</s:if>							
							<table class="tg" id="maptable" border="3">
								<thead>
									<tr>
										<th bgcolor="#cc3333" class="tg-yw4l">ID</th>
										<th bgcolor="#cc3333" class="tg-yw4l">DB Field</th>
										<th bgcolor="#cc3333" class="tg-yw4l">CSV Field</th>
										<th bgcolor="#cc3333" class="tg-yw4l">Static Field</th>
										<th bgcolor="#cc3333" class="tg-yw4l">Type</th>
									</tr>
								</thead>
								<tbody>
									<s:if test="%{#session.CSVDBMap == null}">
									<s:iterator value="%{#session.columns}" status="stat">
										<tr class="trclass">
											<td><s:property value="#stat.index+1" /></td>
											<td class="dbfieldclass"><s:property value="key" /></td>
											<td class="csvfieldclass" id="mapTd<s:property value="#stat.index+1"/>"></td>
											<td class="staticfieldclass" id="mapStatic<s:property value="#stat.index+1"/>"></td>
											<td class="dataTypeClass"><s:property value="value" /></td>
										</tr>
									</s:iterator>
									</s:if>
									<s:if test="%{#session.CSVDBMap != null}">
									
									<s:iterator value="%{#session.CSVDBMap}" status="stat">
  									 <tr>
  									 <td><s:property value="#stat.index+1" /></td>
	 									 <s:iterator value="value">
												<td><s:property /></td>
										</s:iterator>
										 </tr>
									</s:iterator>
									</s:if>
									
								</tbody>
							</table>
							<br />
							<br />	
						</div>						
					</div>
					
				</div>
			</div>
			<div class="container">
				<div id="csv" align="center">
					<h3>Select CSVFile to Upload</h3>
						<s:form action="UploadFile" method="post" enctype="multipart/form-data">
							<s:file label="File" name="file"></s:file>
							<s:submit value="Upload"></s:submit>
						</s:form>
							<s:if test="%{#session.csvHeaders != null}">
								<s:select label="Select a CSV Field" headerKey="-1"	headerValue="Select Field" list="%{#session.csvHeaders}" onchange="populateTable(this.value)" />
								<table><tr><td><div id="staticDataMap">
									<input type="text" name="staticData" id="staticData" placeholder="Static field"  />
			       					<input type="button" value="Update" onclick="validate()" /> 
								</div></td></tr></table>
									<s:textfield id="saveAsMap" name="saveAsMap" placeholder="save As Map" onchange="setMapName(this.value)"/>
									<input type="button" value="Save Map" onclick="mapFields()" /><br /><br/><br/><br/>
									
							</s:if><br/><br/><br/>
							<div id="loadmap">
							<input type="button" value="load MAP" onclick="getMapsFromLocation()" />
								<s:if test="mapfilesList != null">
								<s:select label="Select a map" headerKey="-1"	headerValue="Select map" list="mapfilesList" name="mapname" onchange="getmap(this.value)" />
								</s:if>
							</div><br/><br/><br/>
							<s:radio name = "dbOperation" label="DataBase Operation" id="dbOperation" list="#{'1':'Append', '2':'Overwrite'}" onChange="getDBOperation(this.value)"/><br/><br/><br/>
							<input type="button" value="Submit CSV" onclick="submitForm()" />
				</div>			
			</div>			
			<div class="container">
				<div id="ftp">
				<%--<label for="chkNewftp"><input type="checkbox" id="chkNewftp" onclick="ShowHideDivftp(this)" />Use SFTP</label><br />--%>
						<div id="dvftpHostName">FTP Server Host*:<s:textfield id="txtftpHostName" name="ftpHostName" /></div><br />
						<div id="dvftpPortNumber">FTP Port*:<s:textfield id="txtftpPort" name="ftpPortNumber" /></div><br />
						<div id="dvftpUserName">FTP User*:<s:textfield id="txtftpUser" name="ftpUser" /></div><br />
						<div id="dvftpPassword">FTP Password*:<s:password id="txtftpPassword" name="ftpPassword" /></div><br />
						<div id="dvftpRemotedir">FTP Remote Dir*:<s:textfield id="txtftpRemotedir" name="ftpRemotedir" /></div><br />
						<div id="dvftpsubmit"><s:submit value="Submit" onclick="submitFTP()" /></div>		
				</div>
			</div>
		</div>
</body>
</html>