<?php

	require "init.php";
	
	$name = $_POST["name"];
	$description = $_POST["description"];
	$premiere = $_POST["premiere"];
	$category = $_POST["category"];
	$length = $_POST["length"];
	$agelimit = $_POST["agelimit"];
	$trailer = $_POST["trailer"];
	$cinemaCity = $_POST["cinemaCity"];
	$yesPlanet = $_POST["yesPlanet"];
	$poster = $_POST["poster"];
	$available = 1;
	
	$sql = "select * from mycinema_movies where name='$name'";
	
	$result = mysqli_query($con, $sql);
	
	if(mysqli_num_rows($result) > 0) {
		$status = "existmovie";
	} else {
		$sql = "insert into mycinema_movies(name,description,premiere,category,length,agelimit,trailer,cinemaCity,yesPlanet,poster,available) values('$name','$description','$premiere','$category','$length','$agelimit','$trailer','$cinemaCity','$yesPlanet','$poster',$available)";
	
		if(mysqli_query($con,$sql)){
			$status = "created";
		} else {
			$status = "error";
		}
	}
	
	echo json_encode(array("response"=>$status));
	mysqli_close($con);
?>