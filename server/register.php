<?php

	require "init.php";
	
	$email = $_POST["email"];
	$username = $_POST["username"];
	$password = $_POST["password"];
	$type = "user";
	
	$sql_1 = "select * from mycinema_users where email='$email'";
	$sql_2 = "select * from mycinema_users where username='$username'";
	
	$result_1 = mysqli_query($con, $sql_1);
	$result_2 = mysqli_query($con, $sql_2);
	
	if(mysqli_num_rows($result_1) > 0) {
		$status = "existemail";
	} elseif(mysqli_num_rows($result_2) > 0) {
		$status = "existusername";
	} else {
		$sql = "insert into mycinema_users(email,username,password,type) values('$email','$username','$password','$type')";
	
		if(mysqli_query($con,$sql)){
			$status = "created";
		} else {
			$status = "error";
		}
	}
	
	echo json_encode(array("response"=>$status));
	mysqli_close($con);
?>