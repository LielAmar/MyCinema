<?php

	require "init.php";
	
	$sql = "select * from mycinema_movies";
	$result = mysqli_query($con, $sql);
	
	$list = "";
	
	if(mysqli_num_rows($result) > 0) {
		while($row = $result->fetch_assoc()) {
			$old = $list;
			
//			$imagedata = file_get_contents("pictures/" . $row["name"] . ".JPG");
//			$base64 = base64_encode($imagedata);		
//			$list = $old . "id: " .$row["id"] . ", name: " . $row["name"] . ", description: " . $row["description"] . ", premiere: " . $row["premiere"] . ", category: " . $row["category"] . ", length: " . $row["length"] . ", agelimit: " . $row["agelimit"] . ", trailer: " . $row["trailer"] . ", cinemaCity: " . $row["cinemaCity"] . ", yesPlanet: " . $row["yesPlanet"] . ", poster: " . $base64 . ";";
			if($row["available"] == 1) {
				$list = $old . "id: " .$row["id"] . ", name: " . $row["name"] . ", description: " . $row["description"] . ", premiere: " . $row["premiere"] . ", category: " . $row["category"] . ", length: " . $row["length"] . ", agelimit: " . $row["agelimit"] . ", trailer: " . $row["trailer"] . ", cinemaCity: " . $row["cinemaCity"] . ", yesPlanet: " . $row["yesPlanet"] . ", poster: " . $row["poster"] . ";";
			}
		}
	}
	
	echo json_encode(array("response"=>$list));
	mysqli_close($con);
?>