
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8"/>
</head>
<?php
	//这里我们获取用户提交的信息
	
	require_once 'common.php';
	$pd_FrpId=$_REQUEST['pd_FrpId']; //银行
	$p2_Order=$_REQUEST['p2_Order']; //订单号
	$p3_Amt=$_REQUEST['p3_Amt'];	//金额
	
	
	$p0_Cmd="Buy"; //业务类型，固定 buy
	$p1_MerId="10001126856"; // 商户编号  
	$p4_Cur="CNY"; 	// 交易币种 是  Max(10)  固定值 ”CNY 	
//商品	
	$p5_Pid="";	//商品名称
	$p6_Pcat="";//种类
	$p7_Pdesc="";//商品介绍
	
	//这是易宝支付成功后，给url返回信息 成功才会回复
	$p8_Url="http://localhost/YiBaoMoney/res.php";
	// 商户接收支付成功数据的地址 否  Max(200) 支付成功后易宝支付会向该地址发送两次成功通知，
	//该地址可以带参数，如:“ www.yeepay.com/callback.action?test=test”.
	//注意：如不填p8_Url的参数值支付成功后您将得不到支付成功的通知。 
	
	$p9_SAF="0";//送货地址 否  Max(1) 为“1”: 需要用户将送货地址留在易宝支付系统;为“0”: 不需要，默认为 ”0”. 
	$pa_MP=""; //商户扩展信息 否  Max(200)  返回时原样返回，此参数如用到中文，请注意转码. 
	$pr_NeedResponse="1";
		//应答机制 	否  Max(1) 固定值为“1”: 需要应答机制; 
		//收到易宝支付服务器点对点支付成功通知，必须回写以”success”（无关大小写）开头的字符串，
		//即使您收到成功通知时发现该订单已经处理过，也要正确回写”success”，
		//否则易宝支付将认为您的系统没有收到通知，启动重发机制，直到收到”success”为止。
 


	//我们把请求参数一个一个拼接(拼接的时候，顺序很重要!!!!)
	$data="";
	$data=$data.$p0_Cmd;
	$data=$data.$p1_MerId;
	$data=$data.$p2_Order;
	$data=$data.$p3_Amt;
	$data=$data.$p4_Cur;
	$data=$data.$p5_Pid;
	$data=$data.$p6_Pcat;
	$data=$data.$p7_Pdesc;
	$data=$data.$p8_Url;
	$data=$data.$p9_SAF;
	$data=$data.$pa_MP;
	$data=$data.$pd_FrpId;
	$data=$data.$pr_NeedResponse;
	
	
	// $merchantKey 为秘钥 我们之前准备好的
	$merchantKey	= "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
	//产生hmac需要两个参数，并调用相关API.
	//参数1: STR，列表中的参数值按照签名顺序拼接所产生的字符串，注意null要转换为 ””，并确保无乱码.
	
	
	//hmac是签名串，是用于易宝和商家互相确认的关键字
	//这里我们需要使用算法来生成( md5-hmac算法)
	
	//我们把请求参数一个一个拼接(拼接的时候，顺序很重要!!!!)	
	$hmac=HmacMd5($data,$merchantKey);
	


?>

你的订单号是<?php echo $p2_Order; ?> 支付的金额是<?php echo $p3_Amt;?>
<!--把要提交的数据用隐藏域表示-->
<form action="https://www.yeepay.com/app-merchant-proxy/node" method="post">
<input type="hidden" name="p0_Cmd" value="<?php echo $p0_Cmd;?>"/>
<input type="hidden" name="p1_MerId" value="<?php echo $p1_MerId; ?>"/>
<input type="hidden" name="p2_Order" value="<?php echo $p2_Order; ?>"/>
<input type="hidden" name="p3_Amt" value="<?php echo $p3_Amt; ?>"/>
<input type="hidden" name="p4_Cur" value="<?php echo $p4_Cur;?>"/>
<input type="hidden" name="p5_Pid" value="<?php echo $p5_Pid?>"/>
<input type="hidden" name="p6_Pcat" value="<?php echo $p6_Pcat;?>"/>
<input type="hidden" name="p7_Pdesc" value="<?php echo $p7_Pdesc;?>"/>
<input type="hidden" name="p8_Url" value="<?php echo $p8_Url;?>"/>
<input type="hidden" name="p9_SAF" value="<?php echo $p9_SAF;?>"/>
<input type="hidden" name="pa_MP" value="<?php echo $pa_MP;?>"/>
<input type="hidden" name="pd_FrpId" value="<?php echo $pd_FrpId;?>"/>
<input type="hidden" name="pr_NeedResponse" value="<?php echo $pr_NeedResponse;?>"/>
<input type="hidden" name="hmac" value="<?php echo $hmac;?>"/>
<input type="submit" value="确认网上支付"/>
</form>
</html>

</html>
