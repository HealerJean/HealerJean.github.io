
<?php
	//echo "支付成功!"; 

	include_once 'common.php';
	//获取从易宝支付网关返回的信息
	//$p1_MerId=

	$p1_MerId="10001126856";//就是自己的商号.
	 
	$r0_Cmd=$_REQUEST['r0_Cmd'];
	$r1_Code=$_REQUEST['r1_Code'];
	$r2_TrxId=$_REQUEST['r2_TrxId'];
	$r3_Amt=$_REQUEST['r3_Amt'];
	$r4_Cur=$_REQUEST['r4_Cur'];
	$r5_Pid=$_REQUEST['r5_Pid'];
	$r6_Order=$_REQUEST['r6_Order'];
	$r7_Uid=$_REQUEST['r7_Uid'];
	$r8_MP=$_REQUEST['r8_MP'];
	$r9_BType=$_REQUEST['r9_BType'];
	$hmac=$_REQUEST['hmac'];

	//拼接
	$res_src="";
	$res_src=$res_src.$p1_MerId;
	$res_src=$res_src.$r0_Cmd;
	$res_src=$res_src.$r1_Code;
	$res_src=$res_src.$r2_TrxId;
	$res_src=$res_src.$r3_Amt;
	$res_src=$res_src.$r4_Cur;
	$res_src=$res_src.$r5_Pid;
	$res_src=$res_src.$r6_Order;
	$res_src=$res_src.$r7_Uid;
	$res_src=$res_src.$r8_MP;
	$res_src=$res_src.$r9_BType;
	$merchantKey	= "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
	//对返回的结果进行md5-hmac加密处理，和返回的hmac签名串比较
	
	if(HmacMd5($res_src,$merchantKey)==$hmac){
		if($r1_Code==1){ 
			if($r9_BType==1){
				echo '交易成功!';
				echo '订单号为'.$r6_Order.'支付成功!'.'所付金额是'.$r3_Amt."易宝支付订单号".$r2_TrxId;
				echo '<br/>浏览器重定向';
			}elseif($r9_BType==2){

				echo 'success';
				echo '<br/>交易成功!';
				echo '<br/>服务器点对点通讯';
			}
		}
	}else{
		echo "签名被篡改";
	} 
?>

