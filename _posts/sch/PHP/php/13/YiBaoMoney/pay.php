
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8"/>
</head>
<form action="payConfrim.php" method="post">
<table>
<tr>
<td colspan="4">
订单号:<input type="text" name="p2_Order"/> 
支付金额:<input type="text" name="p3_Amt"/></td>
</tr>
<tr><td colspan="4">请选择支付银行</td></tr>
<tr>
<!-- 这里的银行的名字是不可以随便写的 -->
<td><input type="radio" name="pd_FrpId" value="CMBCHINA-NET"/>招商银行</td>
<td><input type="radio" name="pd_FrpId" value="ICBC-NET"/>工商银行</td>
<td><input type="radio" name="pd_FrpId" value="ABC-NET"/>农业银行</td>
<td><input type="radio" name="pd_FrpId" value="CCB-NET"/>建设银行</td>
</tr>
<tr>
<td colspan="4"><input type="submit" value="确认支付"/></td>
</tr>
</table>
</form>
</html>
