<?php
/* 
 * //�Զ��� ������
	//������һ�������������ڴ������ĺ�����
	function my_error($errno,$errmes){
		echo "<font size='5' color='red'>$errno</font><br/>";
		echo "������Ϣ��:";
		exit();
	}

	//��дset_error_handler������
	//������仰�ĺ����� �� ��������� E_WARNING�������Ĵ���,��ȥ����my_error����.
	set_error_handler("my_error",E_WARNING);

	$fp=fopen("aa.txt","r");
	 */

//�Զ��崥����,ʱ��ͬʱָ�����󼶱�. 

		function  my_error3($errno,$errmes){
			echo "�������:".$errno;
			}

	/* 	function my_error4($errno,$errmes){
			echo "��������".$errno;
			echo "�������ϢΪ".$errmes;
			exit();
		}
		  */
		//ָ��E_USER_WARNING ���󼶱�ĺ��� 
		set_error_handler("my_error3",E_USER_WARNING);
		set_error_handler("my_error4",E_USER_ERROR);
		
		
		$age=700; 
		if($age>120){
			//�Զ��崥����,ʱ��ͬʱָ�����󼶱�. 
			trigger_error("�����������1",E_USER_ERROR);
			//exit(); 
		}
		 
		echo "ok";
	

//дһ����ʵ���ļ�
	function  my_error4($errno,$errmes){
		$err_info="�������:".$errno."--".$errmes;
		echo $err_info; 
		//�����������Ϣ����
		//\r\n ��ʾ���ļ�����һ���س�����
		//<br/> ��ʾ����ҳ���һ���س����� 
		
		error_log($err_info."\r\n",3,"d:/myerr.txt");
	}
 
?>
