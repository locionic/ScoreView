<?php
	session_start();
	session_unset();
	session_destroy();
	echo 'You have completely signed out of our website. Please consider to close the window.';
	header("refresh: 1; url=signin.php");
?>