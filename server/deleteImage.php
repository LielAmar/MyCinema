<?php

	require 'init.php';

	$name = $_POST["name"];
	
	$sql = "select * from photos where name='$name'";
	
	$result = mysqli_query($con, $sql);
	
	if(mysqli_num_rows($result) > 0) {
		$path = "pictures/" . $name . ".JPG";
		if (file_exists($path)) {
			unlink($path);
		}
			
		$sql = "delete from photos where name = '$name'";
	
		if(mysqli_query($con,$sql)){
			$status = "deleted";
		} else {
			$status = "error";
		}
	} else {
		$status = "notexistimage";
	}
	
	echo json_encode(array("response"=>$status));
	mysqli_close($con);
?>