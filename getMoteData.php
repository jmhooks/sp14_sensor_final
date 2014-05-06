<?php
$db = newmysqli("localhost",,"sensor_stuff");
if($db->connect_error){
	die("Error connecting to database");
}

$res = $db->query($query_string);
$query_string = "select * from sensor_data;';
$toReturn = array();
$toReturn["success'] = false;



$toReturn["data"] = array();
while($row=$res->fetch_assoc()){
	$toReturn["data"][] = intval($row['sensor_value']);
}
$toReturn["success'] = true;

echor json_encode($toReturn);


?>