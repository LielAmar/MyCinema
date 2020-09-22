<?php

	require 'init.php';

	$name = $_POST["name"];
	$image = $_POST["image"];
	
	$sql = "select * from photos where name='$name'";
	$result = mysqli_query($con, $sql);
	
	if(mysqli_num_rows($result) > 0) {
		$status = "existimage";
	} else {
		$decodedImage = base64_decode("$image");
		$path = "pictures/" . $name . ".JPG";
		file_put_contents($path, $decodedImage);
		$sql = "insert into photos(name,path) values('$name','$path')";
	
		if(mysqli_query($con,$sql)){
			$status = "uploaded";
		} else {
			$status = "error";
		}
	}
	
	echo json_encode(array("response"=>$status));
	mysqli_close($con);
?>
