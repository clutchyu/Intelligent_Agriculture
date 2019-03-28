<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>欢迎来到智能农业管理系统</title>
<link rel="stylesheet" href="css/button.css">
</head>
<body background="D:\background.jpg">
<hr />
<br>
<br>
<%@ page language="java" import="java.util.*,java.sql.ResultSet,com.mysql.jdbc.Statement"%>
<%@ page import="java.util.Map" %>
<%@ page import="jdbcUtil.JdbcUtil"%>
<%
    String sql="select * from atable;";
	Statement statement = (Statement)new jdbcUtil.JdbcUtil().getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	ResultSet rs = statement.executeQuery(sql);
	//String s[]=new String[5];
	String soil[]=new String[5];
	String home1[]=new String[5];
	String home2[]=new String[5];
	int light[]=new int[5];
	rs.last();
	int len=rs.getRow();
	rs.previous();
	rs.previous();
	rs.previous();
	rs.previous();
	for(int i=0;i<5;i++){
		soil[i]=rs.getString("soil_humidity");
		home1[i]=rs.getString("humidity");
		home2[i]=rs.getString("temperature");
		light[i]=rs.getInt("light");
		rs.next();
	}
	rs.close();
%>
<h1><center><b><font color="white">欢迎来到智能农业管理系统</font></b></center></h1>
<br />
<table border="7" background="white" align="center">
    <tr height="50">
		<td width="150"  align="center"><font color="white"><b>项目</b></font></td>
		<td align="center" width="200"><font color="white">土壤干燥度</font></td>
		<td align="center" width="200"><font color="white">大棚湿度</font></td>
		<td align="center" width="200"><font color="white">大棚温度</font></td>
		<td align="center" width="200"><font color="white">光照强度</font></td>
 
    </tr>
    <tr height="40">
		<td  align="center"><font color="white"><b>当前值</b></font></td>
		<td  align="center"><font color="white"><%=soil[4]%></font></td>
		<td  align="center"><font color="white"><%=home1[4]%></font></td>
		<td  align="center"><font color="white"><%=home2[4]%></font></td>
		<td  align="center"><font color="white"><%=light[4]%></font><td>
	</tr>
	<tr height="40">
		<td align="center" rowspan="5"><font color="white"><b>历史记录</b></font></td>
		<td align="center"><font color="white"><%=soil[0]%></font></td>
		<td align="center"><font color="white"><%=home1[0]%></font></td>
		<td align="center"><font color="white"><%=home2[0]%></font></td>
		<td align="center"><font color="white"><%=light[0]%></font></td>
	</tr>
	<tr height="40">
		<td align="center"><font color="white"><%=soil[1]%></font></td>
		<td align="center"><font color="white"><%=home1[1]%></font></td>
		<td align="center"><font color="white"><%=home2[1]%></font></td>
		<td align="center"><font color="white"><%=light[1]%></font></td>
	</tr>
	<tr height="40">
		<td align="center"><font color="white"><%=soil[2]%></font></td>
		<td align="center"><font color="white"><%=home1[2]%></font></td>
		<td align="center"><font color="white"><%=home2[2]%></font></td>
		<td align="center"><font color="white"><%=light[2]%></font></td>
	</tr>
	<tr height="40">
		<td align="center"><font color="white"><%=soil[3]%></font></td>
		<td align="center"><font color="white"><%=home1[3]%></font></td>
		<td align="center"><font color="white"><%=home2[3]%></font></td>
		<td align="center"><font color="white"><%=light[3]%></font></td>
	</tr>
	<tr height="40">
		<td align="center"><font color="white"><%=soil[4]%></font></td>
		<td align="center"><font color="white"><%=home1[4]%></font></td>
		<td align="center"><font color="white"><%=home2[4]%></font></td>
		<td align="center"><font color="white"><%=light[4]%></font></td>
	</tr>
</table>
<%

%>

<form action="/intelligent_agriculture/Led_Test" method="post" >
<div align="center" class="container">
	<br><br>
	<input name="op1" type="submit" value="开小灯"><br>
	<input name="op2" type="submit" value="关小灯"><br>
	<input name="op3" type="submit" value="关风扇"><br>
	<input name="op4" type="submit" value="开风扇"><br>
	<input name="op5" type="submit" value="开纱帘"><br>
	<input name="op6" type="submit" value="关纱帘"><br>
	<input name="op7" type="submit" value="自动"><br>
	<input name="op8" type="submit" value="手动"><br>
	</div>
	
</form>

<%  
  //页面每隔3秒自动刷新一遍       
 // response.setHeader("refresh" , "3;URL=/intelligent_agriculture/jsp/index1.jsp" );  
  
%>
</body>
</html>