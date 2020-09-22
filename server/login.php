<?php
	require "init.php";

	$email = $_POST["email"];
	$password = $_POST["password"];
	
	$sql_username = "select username from mycinema_users where email = '$email' and password = '$password'";
	
	$result = mysqli_query($con, $sql_username);
	
	if(!mysqli_num_rows($result)>0) {
		$status = "failed";
		echo json_encode(array("response"=>$status));
	} else {
		$row = mysqli_fetch_assoc($result);
		$username = $row['username'];
			
		$sql_type = "select type from mycinema_users where email = '$email' and password = '$password'";
		$result = mysqli_query($con, $sql_type);
		$row = mysqli_fetch_assoc($result);
		$type = $row['type'];

			
		$status = "ok";
		echo json_encode(array("response"=>$status,"email"=>$email,"username"=>$username,"type"=>$type));
	}
	
	mysqli_close($con);
	
?>