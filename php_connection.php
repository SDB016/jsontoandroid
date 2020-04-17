<?php
 $con=mysqli_connect("localhost","root","","info");
 if(mysqli_connect_errno($con)){
   echo "Failed";
 }



 mysqli_set_charset($con,"utf8");

 $res = mysqli_query($con,"select * from `userinfo`");

 $result = array();

 while($row = mysqli_fetch_array($res)){

   array_push($result,array('name'=>$row[0],'gender'=>$row[1],'class'=>$row[2]));
 }
 echo json_encode(array("result"=>$result),JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);

 mysqli_close($con);
 ?>
