<?php
include 'JSON.php'
include 'db_helpers.php'

$json = new Services_JSON();
$result = run();
echo $json->encode($result);

function run(){
	$result['success'] = false;
	if(!dbConnect()){
		$result['reason'] = 'could not connect o database';
		return $result;
		
	}
	$id = $_POST["id"];
	$radio_address = $_POST["radio_address"];
	$mote_id = $_POST["mote_id"];
	$sensor_value = $_POST["sensor_value"];
	$latlong = $_POST["latlong"];



$query = "INSERT INTO sensor_data( id,radio_address,mote_id,sensor_value,latlong) " . "VALUES('$id','$radio_address','$mote_id','$sensor_value','$latlong')";

if(!$results){
	$result['reason']= "error inserting informtion into database";
	return $result;
}

$result['success']=true;
$result['reason']="succesfully inserted information into database";
}
?>