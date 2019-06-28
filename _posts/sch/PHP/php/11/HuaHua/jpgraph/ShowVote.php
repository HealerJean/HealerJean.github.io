<?php // content="text/plain; charset=utf-8"
require_once ('jpgraph/jpgraph.php');
require_once ('jpgraph/jpgraph_bar.php');

$datay1=array(13,8,19,7,17,6);
$datay2=array(4,5,2,7,5,25);

// Create the graph.
$graph = new Graph(350,250);
$graph->SetScale('textlin');
$graph->SetMarginColor('white');

// Setup title
if(!empty($_GET['id'])){
	if($_GET['id']==1){
		
		$str = " zhang yu jin  de tong ji tu ";
		
	}else if($_GET['id']==2){
		$str = " zhao chun yu   de tong ji tu ";
		
	}
}else {
	
	$str = " meiyou chuan ru id ";
	
}

$graph->title->Set($str);
//$graph->title->SetFont(FF_SIMSUN,FS_BOLD,14);
//$graph->title->SetFont(FF_SIMSUN,FS_BOLD,12);
//$graph->title->SetFont(FF_SIMSUN,FS_NORMAL,14);
// Create the first bar
$bplot = new BarPlot($datay1);
$bplot->SetFillGradient('AntiqueWhite2','AntiqueWhite4:0.8',GRAD_VERT);
$bplot->SetColor('darkred');

// Create the second bar
$bplot2 = new BarPlot($datay2);
$bplot2->SetFillGradient('olivedrab1','olivedrab4',GRAD_VERT);
$bplot2->SetColor('darkgreen');

// And join them in an accumulated bar
$accbplot = new AccBarPlot(array($bplot,$bplot2));
$graph->Add($accbplot);

$graph->Stroke();
?>
