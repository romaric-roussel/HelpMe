<?php
//zlpLHP-R0u3V
//psqt_help_me
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: X-Requested-With');
header('Content-type: application/json; charset=UTF-8');

$typeReqHttp = $_SERVER['REQUEST_METHOD'];
$arrayUri = getArrayUri();

$dsn = "mysql:##HOST##;dbname=##DB_NAME##;charset=utf8";
$pdo = new PDO($dsn,"##USER##","##PASSWORD##");

if($typeReqHttp == "POST")
{
  if($arrayUri[1] == "message" && $arrayUri[2] == "")
  {
    if(isset($_POST["message"],$_POST["author"]))
    {
      $stmt = $pdo->prepare("INSERT INTO message (author, message, date_r) VALUES (:author,:message,:date_r)");
      $stmt->execute(array('author'=>$_POST["author"],'message'=>$_POST["message"],'date_r'=>date("Y-m-d H:i:s")));
      echo json_encode(["status"=>"ok"]);
    }
    else
    {
      echo json_encode(["status"=>"no message is available"]);
    }
  }
  else
  {
    echo json_encode(["status"=>"route request is not available"]);
  }
}
else if($typeReqHttp == "GET")
{
  if($arrayUri[1] == "message" && $arrayUri[2] == "")
  {
      $stmt = $pdo->prepare("SELECT * FROM message");
      $stmt->execute();
      $res = $stmt->fetchAll(PDO::FETCH_ASSOC);
      if($res == false)
      {
        echo json_encode(["status"=>"no message is available"]);
      }
      else
      {
        $str = "";
        foreach($res as $row)
        {
          $str = $str . $row["id"] . ":". $row["author"] . ":" . $row["message"] . "&";
        }
        echo $str;
      }
  }
  else if($arrayUri[1] == "message" && $arrayUri[2] == "delete")
  {
    if(isset($_GET["id"]))
    {
      $stmt = $pdo->prepare("DELETE FROM message WHERE id = :id");
      $stmt->execute(array('id'=>$_GET["id"]));
      $res = $stmt->fetchAll(PDO::FETCH_ASSOC);
      var_dump($res);
    }
  }
  else
  {
    echo json_encode(["status"=>"route request is not available"]);
  }
}
else
{
  echo json_encode(["status"=>"type request is not available"]);
}



function getArrayUri()
{
  $basepath = implode('/', array_slice(explode('/', $_SERVER['SCRIPT_NAME']), 0, -1)) . '/';
  $uri = substr($_SERVER['REQUEST_URI'], strlen($basepath));
  if (strstr($uri, '?')) $uri = substr($uri, 0, strpos($uri, '?'));
  $uri = '/' . trim($uri, '/');
  $routes = array();
  $routes = explode('/', $uri);
  return $routes;
}
?>
