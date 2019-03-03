<?php
	session_start();
	if(empty($_SESSION)){
		header("location: signin.php");
		die();
	}
?>
<html>
<head>
	<meta charset="UTF-8">
	<title>Result</title>
  <style>
	.button {
		background-color: #4CAF50;
		border: none;
		color: white;
		padding: 15px 32px;
		text-align: center;
		text-decoration: none;
		display: inline-block;
		font-size: 16px;
		margin: 4px 2px;
		cursor: pointer;
	}
  </style>
</head>

<body style="font-size:125%;font-family:Arial, Helvetica, sans-serif;">
  <h1>Result</h1>

  <p>
		Welcome <em><?php echo $_SESSION['username']; ?></em> to the portal!
  </p>
  <?php
	$url = "http://localhost:8080/ScoreView/rest/scoreview/get-data/" . $_SESSION['username'];
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_POST, 1);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
	$data = curl_exec($ch);
	curl_close($ch);
	$data = json_decode($data);
  ?>
  <?php
	$url = "http://localhost:8080/ScoreView/rest/scoreview/get-data2/".$data->subid;
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_POST, 1);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
	$data2 = curl_exec($ch);
	curl_close($ch);
	$data2 = json_decode($data2);
  ?>
  <?php
	$resp="";
	if(isset($_POST['a1'])){
		$ch = curl_init();
		$url = "http://localhost:8080/ScoreView/rest/scoreview/application-form/".$data->subid."/".$data->id;
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_POST,1);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
		$resp = curl_exec($ch);
		echo $resp;
		curl_close($ch);
	}
  ?>
  
  <form action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="post" id="myForm">
	<input type="text" placeholder="id" name="student_id" disabled value="<?php echo $data->id ?>"/>
	<br/><br/>
	<div>
		<input type="text" placeholder="subid" name="subj_id" disabled value="<?php echo $data->subid ?>"/>
		<input type="submit" name="a1" value="Nop don phuc thao"/>

	</div>	
	<br/><br/>
	<input type="text" placeholder="score" name="score" disabled value="<?php echo $data2->score ?>"/>
	<br/><br/>
	<input type="submit" name="submit"  value="Submit"/>
  </form>
  
  <p>
		Click here to clean <a href="logout.php" tite="Logout">Session</a>
  </p>
</body>
</html>
