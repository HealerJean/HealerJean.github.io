<?php

  	echo "zhang";
    echo  "<br>";

    echo "hello" ;
    echo  "<br>";

  $a=890; 
  var_dump($a); //������ʾ��������Ϣ
  $a=1.1; //�������ǵĶ�$a�ͱ����СС������
  echo  "<br>";
  var_dump($a); //������ʾ��������Ϣ

//1. ���� �����ź�˫����
	$abc = '�����Դ���$a';
	$abc1 = "���������$a";
	echo "".$abc;
	echo ''.$abc1;
	
	echo  "<br><br>*******************�������ݿ�*************************<br><br><br>";
	
//2.  �������ݿ�
	$con = mysql_connect('localhost','root','147094');
	if ($con) {
		echo "���ݿ�������";
	}else {
		echo  "û�����ݿ�";
	}
	echo  "<br><br>********************���Գ���************************<br><br><br>";
	
//3.	���Գ���

	$chu = 7/2;
	echo  $chu;
	echo  "<br><br>********************instanceof�ж��ǲ���һ����************************<br><br><br>";
	
//4.instanceof�ж��ǲ���һ����

	class  cat{};
	class  ma{};
	
	$ma = new cat;
	
	if($ma instanceof cat){
		
		echo  "instanceof�ǲ��Գɹ� ";
		echo  "<br><br><br><br>****************require �����ļ�****************************<br><br><br>";
		
	}else {
		echo "����һ��è";
		echo  "<br><br><br><br>*****************require �����ļ�***************************<br><br><br>";
		
	}
	
	
//5. require �����ļ�

	require "test.php";
	require "test.php";
	require_once 'test.php';
	 
	include 'test.php'; 
	include_once 'test.php';
	
//6. ����
	echo  "<br><br><br><br>*****************require �����ļ�***************************<br><br><br>";

//ֱ������
	$str = array(1,'123',456,'123123132132');
	
	echo count($str);
	 
	for ($i=0;$i< count($str);$i++){
		 
		echo "<br>".$str[$i];
	} 
//����С���Լ�����
	echo  "<br><br><br><br>*************foreach *****************<br><br><br>";
	
	
/* 	
 * $arr['login'] = '��¼';
	$arr['����']='beijing';
 */	
	$arr = array("����"=>"fadf","fas"=>123);
	
	foreach ($arr as $var){
		
		echo "<br>".$var;
	}
	
	
	
//���±�Ҳ�����
	foreach ($arr as $key=>$var){ 
	
		echo "<br>".$key."=".$var;
	}
	
	$arr2 = array("fadf",3=>123); 
	echo  "***********".$arr2[3]."***********8"; 
	foreach ($arr2  as $key=>$var){
	
		echo "<br>".$key."=".$var;
	}
	
	echo  "<br><br><br><br>***********************<br><br><br>";
//��ʾ����	
	print_r($arr);
//��ʾ���ݵ���ϸ��Ϣ
	var_dump($arr);
	
	
// 6 �ж��ǲ�������	
	
	echo  "<br><br><br><br>************  �ж��ǲ�������	 *****************<br><br><br>";
	
	
	echo  is_array($arr);  //��������� ���� 1
	
	
	
// 7 explode(" ", $str3)����ַ��� �õ�����
	echo  "<br><br><br><br>************  explode����ַ��� �õ�����	 *****************<br><br><br>";
	
	
	
	
	$str3 = "zhang yu jin gao tong zhao chun yu ";
	$str4 = explode(" ", $str3);
	echo  print_r($str4);
	
//8. ɾ�������е�ĳһ��Ԫ��
	echo  "<br><br><br><br>************ ɾ�������е�ĳһ��Ԫ�� *****************<br><br><br>";
	
	$atrr = array(1,2,3,5,4);
	unset($atrr[2]);	//ɾ���˻ᱨ��
	// echo  $atrr[2]."ʲôҲû�о���ɾ����";
	
	
	
	
	
?>