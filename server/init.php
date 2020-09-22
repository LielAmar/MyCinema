<?php
	$host = "localhost";
	$username = "-";
	$password = "-";
	$db_name = "-";
	
	$con = mysqli_connect($host, $username, $password, $db_name);
	
	if($con)
		echo "Connection success ";
	else
		echo "Connection failed";
?>