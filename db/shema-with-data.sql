-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.28 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping data for table vas_sms.action: ~8 rows (approximately)
DELETE FROM `action`;
/*!40000 ALTER TABLE `action` DISABLE KEYS */;
INSERT INTO `action` (`id`, `description`, `api_id`, `service_id`) VALUES
	(1, 'Activate GOV news alert service', 1, 1),
	(2, 'Deactivate GOV news alert service', 2, 1),
	(3, 'Activate prepaid data', 3, 2),
	(4, 'Deactivate prepaid data', 4, 2),
	(6, 'DemoActivation', 147, 5),
	(7, 'DemoDeactivation', 147, 5),
	(102, 'deactivate-s', 147, 144),
	(105, 'Activation', 147, 104),
	(106, 'Deactivation-D', 147, 104),
	(143, 'act test', 2, 2),
	(144, 'Activation', 140, 142),
	(145, 'Deactivation', 141, 142),
	(146, 'Activate', 140, 141),
	(147, 'activate-s', 140, 144);
/*!40000 ALTER TABLE `action` ENABLE KEYS */;

-- Dumping data for table vas_sms.api: ~13 rows (approximately)
DELETE FROM `api`;
/*!40000 ALTER TABLE `api` DISABLE KEYS */;
INSERT INTO `api` (`id`, `description`, `name`, `version`, `xml`) VALUES
	(1, 'GOV news alert activation', 'ActivateGovNewsAlert', '1.0', '<?xml version="1.0" encoding="UTF-8"?><service name=\'ActivateGovNewsAlert\'><block id="0" type="Start" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="1" type="Assign" nodeCType="customNode" pos="430.453125,221"><next-node>return</next-node></block><block id="return" type="Return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"1","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-1source-returnnull"}]</conn></service>'),
	(2, 'GOV news alert deactivation', 'DeactivateGovNewsAlert', '1.0', '<?xml version="1.0" encoding="UTF-8"?><service name=\'DeactivateGovNewsAlert\'><block id="0" type="Start" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="1" type="Branch" nodeCType="customNode" pos="251.25,145"><case id="2" type="Case" nodeCType="customNode" pos="401.25,85"><next-node>return</next-node></case><default id="3" type="default" nodeCType="customNode" pos="401.25,215"><next-node>return</next-node></default></block><block id="return" type="Return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"1","sourceHandle":"source","target":"2","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-2target"},{"source":"2","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-2source-returnnull"},{"source":"3","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-3source-returnnull"},{"source":"1","sourceHandle":"source","target":"3","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-3target"}]</conn></service>'),
	(3, 'Prepaid data service activation', 'ActivatePrepaidData', '1.0', '<?xml version="1.0" encoding="UTF-8"?>'),
	(4, 'Prepaid data service deactivation', 'DeactivatePrepaidData', '1.1', '<?xml version="1.0" encoding="UTF-8"?><service name=\'DeactivatePrepaidData\'><block id="0" type="Start" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="1" type="Assign" nodeCType="customNode" pos="285.453125,287"><next-node>return</next-node></block><block id="return" type="Return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"1","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-1source-returnnull"}]</conn></service>'),
	(140, 'Test Act', 'testing activation', '1.0.0', '<?xml version="1.0" encoding="UTF-8"?>'),
	(141, 'Test DeAct', 'testing deactivation', '1.0.0-RE', '<?xml version="1.0" encoding="UTF-8"?><service name=\'testing deactivation\'><block id="0" type="Start" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="1" type="Branch" nodeCType="customNode" pos="248.453125,225"><case id="2" type="Case" nodeCType="customNode" pos="435.453125,146"><next-node>return</next-node></case><default id="3" type="default" nodeCType="customNode" pos="435.453125,276"><next-node>return</next-node></default></block><block id="return" type="Return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"1","sourceHandle":"source","target":"2","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-2target"},{"source":"1","sourceHandle":"source","target":"3","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-3target"},{"source":"2","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-2source-returnnull"},{"source":"3","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-3source-returnnull"},{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"}]</conn></service>'),
	(142, 'api api', 'test api', 'v1', '<?xml version="1.0" encoding="UTF-8"?><service name=\'test api\'><block id="0" type="Start" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="1" type="Assign" nodeCType="customNode" pos="237.453125,161"><next-node>return</next-node></block><block id="return" type="Return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"0","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-0null-1target"},{"source":"1","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-1source-returnnull"}]</conn></service>'),
	(144, 'sample rest api', 'sample rest ', '1.2.3', '<?xml version="1.0" encoding="UTF-8"?>'),
	(145, 'validate sim pin', 'pin validation', '2.0.3', '<?xml version="1.0" encoding="UTF-8"?><service name=\'pin validation\'><block id="1" type="Assign" nodeCType="customNode" pos="170.453125,163"><next-node>return</next-node></block><block id="0" type="Start" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="return" type="Return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"1","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-1source-returnnull"},{"source":"0","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-0null-1target"}]</conn></service>'),
	(146, 'asd', 'asd', 'a', '<?xml version="1.0" encoding="UTF-8"?><service name=\'asd\'><block id="4" type="assign" nodeCType="customNode" pos="633.453125,89"><next-node>return</next-node></block><block id="0" type="assign" nodeCType="startNode" pos="25,225"><next-node>5</next-node></block><block id="5" type="assign" nodeCType="customNode" pos="112.453125,116"><next-node>1</next-node></block><block id="1" type="branch" nodeCType="customNode" pos="183.453125,224"><default id="2" type="default" nodeCType="customNode" pos="333.453125,164"><next-node>4</next-node></default><default id="3" type="default" nodeCType="customNode" pos="333.453125,294"><next-node>return</next-node></default></block><block id="return" type="return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"1","sourceHandle":"source","target":"2","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-2target"},{"source":"3","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-3source-returnnull"},{"source":"1","sourceHandle":"source","target":"3","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-3target"},{"source":"2","sourceHandle":"source","target":"4","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-2source-4target"},{"source":"4","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-4source-returnnull"},{"source":"0","sourceHandle":null,"target":"5","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-0null-5target"},{"source":"5","sourceHandle":"source","target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-5source-1target"}]</conn></service>'),
	(147, 'This is for demostration', 'DemoApi', '1.0', '<?xml version="1.0" encoding="UTF-8"?><service name=\'DemoApiR\'><block id="0" type="assign" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="1" type="assign" nodeCType="customNode" pos="249.453125,202"><next-node>2</next-node></block><block id="2" type="Function:HTTP" nodeCType="func" pos="587.453125,234"><next-node>return</next-node></block><block id="return" type="return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"0","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-0null-1target"},{"source":"1","sourceHandle":"source","target":"2","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-2target"},{"source":"2","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-2source-returnnull"}]</conn></service>'),
	(154, 'yjtyjhnd75u56c rdy6 rdy', 'chathura', NULL, NULL);
/*!40000 ALTER TABLE `api` ENABLE KEYS */;

-- Dumping data for table vas_sms.api_history: ~0 rows (approximately)
DELETE FROM `api_history`;
/*!40000 ALTER TABLE `api_history` DISABLE KEYS */;
INSERT INTO `api_history` (`ID`, `commit_id`, `commit_message`, `version`, `xml`, `is_active`, `commited_date_time`, `api_id`) VALUES
	(3, '2ae4ef04-d445-4856-95b9-e18c77580c76', 'changed: added new node', '0.1', '<?xml version="1.0" encoding="UTF-8"?><service name=\'DemoApi\'><block id="0" type="assign" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="1" type="assign" nodeCType="customNode" pos="136.453125,163"><next-node>return</next-node></block><block id="return" type="return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"1","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-1source-returnnull"}]</conn></service>', b'0', '2022-08-25 13:48:20', 147),
	(4, 'c0bb14fe-5570-4e7f-a46f-2c3530fa7413', 'test: added new branch', '0.2', '<?xml version="1.0" encoding="UTF-8"?><service name=\'DemoApi\'><block id="0" type="assign" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="1" type="branch" nodeCType="customNode" pos="305.453125,194"><case id="2" type="case" nodeCType="customNode" pos="455.453125,134"><next-node>return</next-node></case><default id="3" type="default" nodeCType="customNode" pos="455.453125,264"><next-node>return</next-node></default></block><block id="return" type="return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"1","sourceHandle":"source","target":"2","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-2target"},{"source":"1","sourceHandle":"source","target":"3","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-3target"},{"source":"2","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-2source-returnnull"},{"source":"3","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-3source-returnnull"}]</conn></service>', b'0', '2022-08-25 14:07:50', 147),
	(5, 'e4eb3c32-9294-4359-a82d-7216644506e7', 'test: new variables', '0.2.1', '<?xml version="1.0" encoding="UTF-8"?><service name=\'DemoApi\'><block id="0" type="assign" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="1" type="assign" nodeCType="customNode" pos="249.453125,256"><next-node>return</next-node></block><block id="return" type="return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"1","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-1source-returnnull"}]</conn></service>', b'0', '2022-08-25 14:09:39', 147),
	(6, 'b1656a2f-d74a-4d63-91a6-0723ff6e1270', 'position change', '1.0', '<?xml version="1.0" encoding="UTF-8"?><service name=\'DemoApi\'><block id="1" type="assign" nodeCType="customNode" pos="494.453125,98"><next-node>return</next-node></block><block id="return" type="return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"1","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-1source-returnnull"}]</conn></service>', b'0', '2022-08-25 14:15:51', 147),
	(7, '876c264c-77e3-4dfc-b2c2-418cdc42f1d0', 'added new variable after case [2]', '1.0-RE', '<?xml version="1.0" encoding="UTF-8"?><service name=\'DemoApi\'><block id="0" type="assign" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="4" type="assign" nodeCType="customNode" pos="701.453125,365"><next-node>return</next-node></block><block id="1" type="branch" nodeCType="customNode" pos="305.453125,194"><case id="2" type="case" nodeCType="customNode" pos="455.453125,134"><next-node>return</next-node></case><default id="3" type="default" nodeCType="customNode" pos="455.453125,264"><next-node>4</next-node></default></block><block id="return" type="return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"1","sourceHandle":"source","target":"2","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-2target"},{"source":"1","sourceHandle":"source","target":"3","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-3target"},{"source":"2","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-2source-returnnull"},{"source":"3","sourceHandle":"source","target":"4","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-3source-4target"},{"source":"4","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-4source-returnnull"}]</conn></service>', b'0', '2022-08-25 16:25:59', 147),
	(10, 'ffd3cdf9-f96e-4cae-9719-fa05564c9381', 'Initial commit', '1.0', '<?xml version="1.0" encoding="UTF-8"?>', b'0', '2022-08-25 17:01:25', 154),
	(11, '019f764d-61e4-454b-9c1b-32afee1768c4', 'added initial flow', '1.1', '<?xml version="1.0" encoding="UTF-8"?><service name=\'chathura\'><block id="0" type="assign" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="4" type="assign" nodeCType="customNode" pos="584.453125,170"><next-node>return</next-node></block><block id="1" type="branch" nodeCType="customNode" pos="159.453125,148"><default id="3" type="default" nodeCType="customNode" pos="309.453125,218"><next-node>4</next-node></default><case id="2" type="case" nodeCType="customNode" pos="309.453125,88"><next-node>4</next-node></case></block><block id="return" type="return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"2","sourceHandle":"source","target":"4","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-2source-4target"},{"source":"4","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-4source-returnnull"},{"source":"3","sourceHandle":"source","target":"4","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-3source-4target"},{"source":"1","sourceHandle":"source","target":"3","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-3target"},{"source":"1","sourceHandle":"source","target":"2","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-2target"}]</conn></service>', b'0', '2022-08-25 17:06:01', 154),
	(12, '7cad9459-4940-4f50-8376-d531c42de102', 'added secondary flow', '1.1.1', '<?xml version="1.0" encoding="UTF-8"?><service name=\'chathura\'><block id="0" type="assign" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="4" type="assign" nodeCType="customNode" pos="584.453125,170"><next-node>return</next-node></block><block id="1" type="branch" nodeCType="customNode" pos="149.453125,200"><default id="3" type="default" nodeCType="customNode" pos="298.453125,313"><next-node>5</next-node></default><case id="2" type="case" nodeCType="customNode" pos="350.453125,102"><next-node>4</next-node></case></block><block id="5" type="branch" nodeCType="customNode" pos="544.453125,351"><case id="6" type="case" nodeCType="customNode" pos="694.453125,291"><next-node>return</next-node></case><default id="7" type="default" nodeCType="customNode" pos="694.453125,421"><next-node>return</next-node></default></block><block id="return" type="return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"2","sourceHandle":"source","target":"4","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-2source-4target"},{"source":"4","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-4source-returnnull"},{"source":"1","sourceHandle":"source","target":"3","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-3target"},{"source":"1","sourceHandle":"source","target":"2","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-2target"},{"source":"3","sourceHandle":"source","target":"5","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-3source-5target"},{"source":"5","sourceHandle":"source","target":"6","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-5source-6target"},{"source":"5","sourceHandle":"source","target":"7","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-5source-7target"},{"source":"6","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-6source-returnnull"},{"source":"7","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-7source-returnnull"}]</conn></service>', b'1', '2022-08-25 17:07:11', 154),
	(13, 'ece4b8cc-176a-4cb5-84d2-4c8ac4699c7b', 'position change', '1.0-RE', '<?xml version="1.0" encoding="UTF-8"?><service name=\'DemoApi\'><block id="0" type="assign" nodeCType="startNode" pos="25,225"><next-node>1</next-node></block><block id="4" type="assign" nodeCType="customNode" pos="622.453125,372"><next-node>return</next-node></block><block id="1" type="branch" nodeCType="customNode" pos="305.453125,194"><case id="2" type="case" nodeCType="customNode" pos="455.453125,134"><next-node>return</next-node></case><default id="3" type="default" nodeCType="customNode" pos="455.453125,264"><next-node>4</next-node></default></block><block id="return" type="return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"1","sourceHandle":"source","target":"2","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-2target"},{"source":"1","sourceHandle":"source","target":"3","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-3target"},{"source":"2","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-2source-returnnull"},{"source":"3","sourceHandle":"source","target":"4","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-3source-4target"},{"source":"4","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-4source-returnnull"}]</conn></service>', b'0', '2022-08-25 17:09:01', 147),
	(14, '57243e50-d8c4-48c0-8d81-b3e5f9177fed', 'added http node', '1.1', '<?xml version="1.0" encoding="UTF-8"?><service name=\'DemoApi\'><block id="0" type="assign" nodeCType="startNode" pos="33,241"><variable name="var1">\'abc\'</variable><variable name="resCode">1</variable><next-node>1</next-node></block><block id="4" type="assign" nodeCType="customNode" pos="605.453125,337"><variable name="var2">\'xyz\'</variable><variable name="var3">\'loc\'</variable><next-node>6</next-node></block><block id="6" type="func" nodeCType="customNode" pos="831.453125,425"><next-node>return</next-node></block><block id="1" type="branch" nodeCType="customNode" pos="305.453125,194"><case id="2" type="case" nodeCType="customNode" pos="446.453125,76"><expression>\'%ses.var1%\'==\'abc\'</expression><next-node>return</next-node></case><default id="3" type="default" nodeCType="customNode" pos="455.453125,264"><next-node>4</next-node></default><case id="5" type="case" nodeCType="customNode" pos="490.453125,170"><expression>\'%ses.var3%\'==\'abc\'</expression><next-node>return</next-node></case></block><block id="return" type="return" nodeCType="returnNode" pos="1025,225"><outputparams name="ses">-1</outputparams></block><conn>[{"source":"start","sourceHandle":null,"target":"1","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-startnull-1target"},{"source":"1","sourceHandle":"source","target":"2","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-2target"},{"source":"1","sourceHandle":"source","target":"3","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-3target"},{"source":"2","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-2source-returnnull"},{"source":"3","sourceHandle":"source","target":"4","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-3source-4target"},{"source":"1","sourceHandle":"source","target":"5","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-1source-5target"},{"source":"5","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-5source-returnnull"},{"source":"4","sourceHandle":"source","target":"6","targetHandle":"target","type":"buttonedge","id":"reactflow__edge-4source-6target"},{"source":"6","sourceHandle":"source","target":"return","targetHandle":null,"type":"buttonedge","id":"reactflow__edge-6source-returnnull"}]</conn></service>', b'1', '2022-08-25 19:39:00', 147);
/*!40000 ALTER TABLE `api_history` ENABLE KEYS */;

-- Dumping data for table vas_sms.app_user: ~0 rows (approximately)
DELETE FROM `app_user`;
/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
INSERT INTO `app_user` (`id`, `created_date`, `email`, `enabled`, `name`, `password`, `username`, `role_id`) VALUES
	(1, '2022-08-05', 'chathura389@gmail.com', b'1', 'chathura', '$2a$10$D61pfzDE9g3wYSRFWzAE2elA1AJYuqrY.ZHcj4fowIzadM6o/cF/2', 'chathura', 1);
/*!40000 ALTER TABLE `app_user` ENABLE KEYS */;

-- Dumping data for table vas_sms.cx_response: ~9 rows (approximately)
DELETE FROM `cx_response`;
/*!40000 ALTER TABLE `cx_response` DISABLE KEYS */;
INSERT INTO `cx_response` (`id`, `res_code`, `res_desc`, `send_to_originated_no`, `sms`, `api_id`) VALUES
	(1, 1, 'success', 1, 'Thank you! Your request will be processed within 4 hours. If not, please call 071 7 010 101.', 3),
	(2, 1, 'ok', 1, 'Thank you! Your request will be processed within 4 hours. If not, please call 071 1 010 101.', 3),
	(3, 2, 'success', 1, 'Sorry, You don\'t have sufficient balance to activate this service', 3),
	(4, 3, 'already active', 1, 'The requested service is already active.', 3),
	(5, 4, 'already deactive', 1, 'Your service will be activated soon.', 3),
	(6, 5, 'tech error', 1, 'Sorry your line is at barred status', 3),
	(7, 6, 'success', 1, 'Sorry, requested service is not available on your package. Call 071 7 010 101', 3),
	(8, 7, 'SMS sample', 1, 'this is mobitel', 3),
	(9, 1, 'success', 1, 'package has successfully deactivated', 4),
	(10, 1, 'success', 1, 'Demo service has successfully activated. Please dial #1777# for more details. Thank you.', 147),
	(111, 2, 'error', 1, 'This service can not activate at this time.', 147);
/*!40000 ALTER TABLE `cx_response` ENABLE KEYS */;

-- Dumping data for table vas_sms.hibernate_sequence: ~0 rows (approximately)
DELETE FROM `hibernate_sequence`;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` (`next_val`) VALUES
	(113);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;

-- Dumping data for table vas_sms.keyword: ~15 rows (approximately)
DELETE FROM `keyword`;
/*!40000 ALTER TABLE `keyword` DISABLE KEYS */;
INSERT INTO `keyword` (`id`, `first_key`, `reg_ex`, `action_id`) VALUES
	(1, 'ACT', '^\\s*ACT\\s+GOVN\\s*$', 1),
	(2, 'DEACT', '^\\s*DEACT\\s+GOVN\\s*$', 2),
	(3, 'OFF', '^\\s*OFF\\s+GOVN\\s*$', 2),
	(4, 'ACT', '^\\s*ACT\\s+2GB\\s*$', 3),
	(5, 'ACT', '^\\s*ACT\\s+4GB\\s*$', 3),
	(6, 'ACT', '^\\s*ACT\\s+8GB\\s*$', 3),
	(7, 'DEACT', '^\\s*DEACT\\s+DATA\\s*$', 4),
	(8, 'ACT', '\\s*ACT\\s*DEMO\\s*', 6),
	(107, 'ACT', '^\\s*ACT\\s+DEMO\\s*$', 105),
	(108, 'ON', '\\s*ON\\s*DEMO\\s*', 105),
	(109, 'DEACT', '^\\s*DEACT\\s+DEMO\\s*$', 106),
	(110, 'OFF', '\\s*OFF\\s*DEMO\\s*', 106),
	(686, 'TEST', '\\s*TEST_1\\s*', 143),
	(687, 'OFF', '\\s*OFF\\s*DATA\\s*', 4),
	(688, 'TEST', '\\s*TEST3\\s*', 3),
	(689, 'TEST', '\\s*TEST4\\s*', 3),
	(690, 'ACT', '\\s*ACT\\s*PIXME\\s*', 144),
	(691, 'OFF', '\\s*OFF\\s*PIXME\\s*', 145),
	(692, 'ON', '^\\s*ON\\s+PIXME\\s*$', 144),
	(693, 'DEACT', '\\s*DEACT\\s*pixme\\s*', 145),
	(694, 'FAF', '\\s*FAF\\s*', 146);
/*!40000 ALTER TABLE `keyword` ENABLE KEYS */;

-- Dumping data for table vas_sms.service: ~8 rows (approximately)
DELETE FROM `service`;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` (`id`, `description`, `name`, `disable`, `disable_sms`, `created_date`) VALUES
	(1, 'NEWS alerts by the government', 'GOV news alert service', b'0', 'This service is temporarily unavailable', '2022-06-06'),
	(2, 'Prepaid data package activation and deactivation', 'Prepaid Voice Service', b'1', 'This service is temporarily unavailable.', '2022-06-06'),
	(3, '4G voice activation and deactivation', '4G voice service', b'0', 'This service is temporarily unavailable', '2022-06-06'),
	(5, 'This is a demonstration VAS', 'DemoService', b'0', 'Requested service is no longer available.', '2022-08-19'),
	(104, 'Demo VAS for act/deact something', 'DemoService-D', b'0', 'This service is no longer available.', '2022-08-25'),
	(139, 'Timebased data and voice service deactivator', 'Timebased deactivator', b'1', 'This service is temporarily unavailable', '2022-06-06'),
	(140, 'MCA missed call alert activation/deactivation', 'MCA missed call alerts', b'1', 'This service is temporarily unavailable', '2022-06-06'),
	(141, 'FAF and My10 call services', 'FAF-MY-10', b'1', 'This service is temporarily unavailable', '2022-06-06'),
	(142, 'PIXme mobile services', 'PIXme service', b'0', 'This service is no longer unavailable', '2022-06-06'),
	(144, 'this is a test service', 'test service', b'0', 'test sms sms', '2022-06-07');
/*!40000 ALTER TABLE `service` ENABLE KEYS */;

-- Dumping data for table vas_sms.sms_history: ~4 rows (approximately)
DELETE FROM `sms_history`;
/*!40000 ALTER TABLE `sms_history` DISABLE KEYS */;
INSERT INTO `sms_history` (`id`, `received_time`, `sent_time`, `msisdn`, `incoming_sms`, `response_sms`) VALUES
	(1, '2022-06-08 12:54:20', '2022-06-08 12:54:21', '0711231231', 'ACT ACT', 'Incorrect message.'),
	(2, '2022-06-08 17:11:20', '2022-06-08 17:11:19', '0711231231', 'ACT DATA', 'Thank you! Your request will be processed within 4 hours. If not, please call 071 7 010 101.'),
	(3, '2022-08-19 10:47:52', '2022-08-19 11:04:37', '0719997831', 'ACT 5GB', 'This service is temporarily unavailable.'),
	(4, '2022-08-19 11:05:08', '2022-08-19 11:05:08', '0719997831', 'ACT 5GB', 'This service is temporarily unavailable.'),
	(112, '2022-08-25 20:53:04', '2022-08-25 20:53:06', '0719997831', 'ACT DEMO', 'Demo service has successfully activated. Please dial #1777# for more details. Thank you.');
/*!40000 ALTER TABLE `sms_history` ENABLE KEYS */;

-- Dumping data for table vas_sms.sys_action_log: ~89 rows (approximately)
DELETE FROM `sys_action_log`;
/*!40000 ALTER TABLE `sys_action_log` DISABLE KEYS */;
INSERT INTO `sys_action_log` (`ID`, `log`, `timestamp`, `user_id`) VALUES
	(12, 'API DemoApi was updated by chathura', '2022-08-23 12:51:46', 1),
	(13, 'API DemoApi was updated by chathura', '2022-08-23 12:51:48', 1),
	(14, 'API DemoApi was updated by chathura', '2022-08-23 12:51:49', 1),
	(15, 'API DemoApi was updated by chathura', '2022-08-23 12:55:24', 1),
	(16, 'API DemoApi was updated by chathura', '2022-08-23 13:01:17', 1),
	(17, 'API DemoApi was updated by chathura', '2022-08-23 13:04:45', 1),
	(18, 'API DemoApi was updated by chathura', '2022-08-23 13:07:32', 1),
	(19, 'API DemoApi was updated by chathura', '2022-08-23 13:23:13', 1),
	(20, 'API DemoApi was updated by chathura', '2022-08-23 13:23:36', 1),
	(21, 'API DemoApi was updated by chathura', '2022-08-23 13:30:53', 1),
	(22, 'API DemoApi was updated by chathura', '2022-08-23 13:31:52', 1),
	(23, 'API DemoApi was updated by chathura', '2022-08-23 13:33:40', 1),
	(24, 'API DemoApi was updated by chathura', '2022-08-23 15:42:55', 1),
	(25, 'API DemoApi was updated by chathura', '2022-08-23 15:59:11', 1),
	(26, 'API DemoApi was updated by chathura', '2022-08-23 16:05:08', 1),
	(27, 'API DemoApi was updated by chathura', '2022-08-23 16:06:57', 1),
	(28, 'API DemoApi was updated by chathura', '2022-08-23 16:28:19', 1),
	(29, 'API DemoApi was updated by chathura', '2022-08-23 16:28:26', 1),
	(30, 'API DemoApi was updated by chathura', '2022-08-23 17:00:26', 1),
	(31, 'API DemoApi was updated by chathura', '2022-08-23 17:15:56', 1),
	(32, 'API DeactivateGovNewsAlert was updated by chathura', '2022-08-23 18:02:48', 1),
	(33, 'API DemoApi was updated by chathura', '2022-08-23 18:07:28', 1),
	(34, 'API DemoApi was updated by chathura', '2022-08-23 18:15:37', 1),
	(35, 'API DemoApi was updated by chathura', '2022-08-23 18:15:45', 1),
	(36, 'API DemoApi was updated by chathura', '2022-08-23 18:17:23', 1),
	(37, 'API DemoApi was updated by chathura', '2022-08-23 18:20:53', 1),
	(38, 'API DemoApi was updated by chathura', '2022-08-23 20:51:25', 1),
	(39, 'API DemoApiX was updated by chathura', '2022-08-24 11:33:14', 1),
	(40, 'API DemoApi was updated by chathura', '2022-08-24 11:35:06', 1),
	(41, 'API DemoApit was updated by chathura', '2022-08-24 11:36:13', 1),
	(42, 'API DemoApi was updated by chathura', '2022-08-24 11:39:44', 1),
	(43, 'API DemoApiR was updated by chathura', '2022-08-24 11:40:54', 1),
	(44, 'API DemoApiR was updated by chathura', '2022-08-24 11:52:56', 1),
	(45, 'API DemoApiR was updated by chathura', '2022-08-24 11:55:21', 1),
	(46, 'API DemoApiR was updated by chathura', '2022-08-24 11:58:31', 1),
	(47, 'API DemoApiR was updated by chathura', '2022-08-24 12:04:45', 1),
	(48, 'API DemoApiR was updated by chathura', '2022-08-24 12:18:15', 1),
	(49, 'API DemoApiR was updated by chathura', '2022-08-24 12:19:05', 1),
	(50, 'API DemoApiR was updated by chathura', '2022-08-24 12:20:34', 1),
	(51, 'API DemoApiR was updated by chathura', '2022-08-24 12:21:31', 1),
	(52, 'API DemoApiR was updated by chathura', '2022-08-24 12:23:49', 1),
	(53, 'API DemoApiR was updated by chathura', '2022-08-24 12:24:28', 1),
	(54, 'API DemoApiR was updated by chathura', '2022-08-24 12:24:33', 1),
	(55, 'API DemoApiR was updated by chathura', '2022-08-24 12:26:14', 1),
	(56, 'API DemoApiR was updated by chathura', '2022-08-24 12:26:53', 1),
	(57, 'API DemoApiR was updated by chathura', '2022-08-24 12:38:03', 1),
	(58, 'API DemoApiR was updated by chathura', '2022-08-24 12:46:14', 1),
	(59, 'API DemoApiR was updated by chathura', '2022-08-24 12:46:30', 1),
	(60, 'API DemoApiR was updated by chathura', '2022-08-24 16:44:56', 1),
	(61, 'API DemoApiR was updated by chathura', '2022-08-24 16:48:09', 1),
	(62, 'API DemoApiR was updated by chathura', '2022-08-24 16:48:15', 1),
	(63, 'API DemoApiR was updated by chathura', '2022-08-24 16:48:28', 1),
	(64, 'API DemoApiR was updated by chathura', '2022-08-24 16:48:58', 1),
	(65, 'API ActivateGovNewsAlert was updated by chathura', '2022-08-24 16:55:05', 1),
	(66, 'API test api was updated by chathura', '2022-08-24 16:57:44', 1),
	(67, 'API test api was updated by chathura', '2022-08-24 17:00:15', 1),
	(68, 'API DeactivatePrepaidData was updated by chathura', '2022-08-24 17:01:18', 1),
	(69, 'API pin validation was updated by chathura', '2022-08-24 17:01:58', 1),
	(70, 'API pin validation was updated by chathura', '2022-08-24 17:02:35', 1),
	(71, 'API testing deactivation was updated by chathura', '2022-08-24 17:03:14', 1),
	(72, 'API DemoApiR was updated by chathura', '2022-08-24 17:28:24', 1),
	(73, 'API DemoApiR was updated by chathura', '2022-08-24 17:39:43', 1),
	(74, 'API DemoApiR was updated by chathura', '2022-08-24 17:41:51', 1),
	(75, 'API DemoApiR was updated by chathura', '2022-08-24 18:32:56', 1),
	(76, 'API DemoApiR was updated by chathura', '2022-08-24 18:56:13', 1),
	(77, 'API DemoApiR was updated by chathura', '2022-08-24 19:02:53', 1),
	(78, 'API DemoApiR was updated by chathura', '2022-08-24 19:11:43', 1),
	(79, 'API DemoApiR was updated by chathura', '2022-08-24 19:16:16', 1),
	(80, 'API DemoApiR was updated by chathura', '2022-08-24 19:18:54', 1),
	(81, 'API asd was updated by chathura', '2022-08-24 19:25:57', 1),
	(82, 'API asd was updated by chathura', '2022-08-24 19:26:38', 1),
	(83, 'API asd was updated by chathura', '2022-08-24 19:27:59', 1),
	(84, 'API asd was updated by chathura', '2022-08-24 19:28:14', 1),
	(85, 'API asd was updated by chathura', '2022-08-24 19:29:47', 1),
	(86, 'API asd was updated by chathura', '2022-08-24 19:30:31', 1),
	(87, 'API asd was updated by chathura', '2022-08-24 19:35:59', 1),
	(88, 'API asd was updated by chathura', '2022-08-24 19:39:12', 1),
	(89, 'API asd was updated by chathura', '2022-08-24 19:41:57', 1),
	(90, 'API asd was updated by chathura', '2022-08-24 19:46:17', 1),
	(91, 'API asd was updated by chathura', '2022-08-24 19:47:36', 1),
	(92, 'API asd was updated by chathura', '2022-08-24 19:47:57', 1),
	(93, 'API asd was updated by chathura', '2022-08-24 19:51:15', 1),
	(94, 'API asd was updated by chathura', '2022-08-24 20:05:30', 1),
	(95, 'API DemoApiR was updated by chathura', '2022-08-25 09:15:36', 1),
	(96, 'API DemoApiR was updated by chathura', '2022-08-25 09:39:22', 1),
	(97, 'API DemoApiR was updated by chathura', '2022-08-25 09:39:45', 1),
	(98, 'API DemoApiR was updated by chathura', '2022-08-25 09:47:57', 1),
	(99, 'API DemoApiR was updated by chathura', '2022-08-25 09:48:18', 1),
	(100, 'API DemoApiR was updated by chathura', '2022-08-25 09:48:59', 1),
	(101, 'API DemoApi was updated by chathura', '2022-08-25 11:56:12', 1);
/*!40000 ALTER TABLE `sys_action_log` ENABLE KEYS */;

-- Dumping data for table vas_sms.sys_config: ~0 rows (approximately)
DELETE FROM `sys_config`;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;

-- Dumping data for table vas_sms.user: ~2 rows (approximately)
DELETE FROM `user`;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`ID`, `username`, `password`, `email`, `name`, `enabled`, `created_date`, `role_id`) VALUES
	(1, 'chathura', '$2a$10$D61pfzDE9g3wYSRFWzAE2elA1AJYuqrY.ZHcj4fowIzadM6o/cF/2', 'chathura@gmail.com', 'chathura samarasinghe', b'1', '2022-06-03', 1),
	(2, 'chathura_a', '$2a$10$Gs4Gr63GM0Lcz6cntCh5DOTAKmpWUWhWEMcsVUUpNgWaotUK/aVqy', 'chathura_a@gmail.com', 'benjamin chathura', b'1', '2022-06-03', 2);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- Dumping data for table vas_sms.user_role: ~2 rows (approximately)
DELETE FROM `user_role`;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`ID`, `role`) VALUES
	(1, 'USER'),
	(2, 'ADMIN');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
