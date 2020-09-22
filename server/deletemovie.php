<?php

	require 'init.php';

	$name = $_POST["name"];
	$email = $_POST["email"];
	$password = $_POST["password"];
	
	$sql = "select * from mycinema_movies where name='$name'";
	
	$sql_username = "select username from mycinema_users where email = '$email' and password = '$password'";
	
	$result = mysqli_query($con, $sql_username);
	
	if(!mysqli_num_rows($result)>0) {
		$status = "wrongUser";
		echo json_encode(array("response"=>$status));
	} else {
		$row = mysqli_fetch_assoc($result);
			
		$sql_type = "select type from mycinema_users where email = '$email' and password = '$password'";
		$result = mysqli_query($con, $sql_type);
		$row = mysqli_fetch_assoc($result);
		$type = $row['type'];
	
		echo "\n" . $type . "\n";
		
		if($type == "admin") {
			$result = mysqli_query($con, $sql);
		
			if(mysqli_num_rows($result) > 0) {
				$available = 0;
				$sql = "update mycinema_movies set available = '$available' where name = '$name'";
#				$sql = "insert into mycinema_movies(available) values($available) where $name ==";
				echo $sql . "\n";
			
				if(mysqli_query($con,$sql)){
					$status = "deleted";
				} else {
					$status = "error";
				}
			} else {
				$status = "movieNotExists";
			}
	
			echo json_encode(array("response"=>$status));
			mysqli_close($con);
				
		} else {
			$status = "userIsNotPermitted";
			echo json_encode(array("response"=>$status));
		}
	}
?>