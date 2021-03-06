if (!Detector.webgl) Detector.addGetWebGLMessage();

if (!SESSION_ID) {
	throw new Error("SESSION_ID not found!");
}
if (!TANK_TYPE) {
	throw new Error("TANK_TYPE found!");
}
if (!TANK_OBJ_SET) {
	throw new Error("TANK_OBJ_SET not found!");
}
console.info("SESSION_ID:", SESSION_ID);
console.info("TANK_OBJ_SET:", TANK_OBJ_SET);

var w = window;
var camera, scene, renderer, stats;
var geometry, material, mesh;

var cameraIsBind = false;

var ownTank;
var OBJ_TANK;
var bullet1;

var OBJECTS_PATH = "/resources/media/js/obj/";
var INTERIOR_SCALE = 1;

var TANK_MANAGER = [];

var backsightDown = false;
var backsight = new THREE.Object3D();

var rechargeLabel = new THREE.Object3D();

var RESOURCE_MANAGER = new ResourceManager();

function ResourceManager() {
	var that = this;
	var total = 0;
	var count = 0;
	var items = [];

	this.start = function() {
		QueryLoader.selectorPreload = "body";
		QueryLoader.withLogo = true;
		QueryLoader.init(function() {
			for (var i in items) {
				var call = items[i];
				call();
			}
		});
	};

	this.add = function(func) {
		total++;
		QueryLoader.additionDownloadsCount++;
		items.push(func);
	};

	this.onLoaded = function(current, total) {
		console.log("onLoaded", current, total);
		QueryLoader.downloadDoneCallback();
	};

	this.onTotalLoaded = function() {
		console.log("onTotalLoaded");

		onAllResourceLoaded();
	};

	this.loaded = function() {
		count++;
		that.onLoaded(count, total);

		if (count >= total) {
			that.onTotalLoaded();
		}
	};
}

var AMBIENT_LIGHT = new THREE.AmbientLight(0x666666);

var LOADING_MANAGER = new THREE.LoadingManager();
LOADING_MANAGER.onProgress = function(item, loaded, total) {
	console.log(item, loaded, total);
};
LOADING_MANAGER.onLoad = function(item, loaded, total) {
	console.log("LOADING_MANAGER onLoad", item, loaded, total);

	onAllResourceLoaded();
};

var OBJ_MTL_LOADER = new THREE.OBJMTLLoader();
var TANKS_MANAGER = new TankManager();

var MATERIAL_RED = new THREE.MeshBasicMaterial({
	color: 0xff0000,
	wireframe: true
});

var MATERIAL_GREEN = new THREE.MeshBasicMaterial({
	color: 0x00ff00,
	wireframe: true
});

var MATERIAL_BLUE = new THREE.MeshBasicMaterial({
	color: 0x0000ff,
	wireframe: true
});

var LINE_BASIC_MATERIAL_BLACK = new THREE.LineBasicMaterial({
	color: 0x000000
});

var LINE_BASIC_MATERIAL_GREEN = new THREE.LineBasicMaterial({
	color: 0x00FF00
});

var LINE_BASIC_MATERIAL_ORANGE = new THREE.LineBasicMaterial({
	color: 0xFF9900
});

var ATMOSPHERE_MODEL = new AtmosphereModel();
var TASK_MANAGER = new TaskManager();
var TANK_INFO_LABEL_CONTROLLER = new TankInfoLabelController();

init();
animate();

var keyHandler = new KeyHandler();
// left
keyHandler.addKeyDownListener([65, 37], 0, keyLeftDownListener);
keyHandler.addKeyUpListener([65, 37], 0, keyLeftUpListener);
// top
keyHandler.addKeyDownListener([87, 38], 0, keyTopDownListener);
keyHandler.addKeyUpListener([87, 38], 0, keyTopUpListener);
// right
keyHandler.addKeyDownListener([68, 39], 0, keyRightDownListener);
keyHandler.addKeyUpListener([68, 39], 0, keyRightUpListener);
// bottom
keyHandler.addKeyDownListener([83, 40], 0, keyBottomDownListener);
keyHandler.addKeyUpListener([83, 40], 0, keyBottomUpListener);
// tower left
keyHandler.addKeyDownListener(90, 0, keyTowerLeftDownListener);
keyHandler.addKeyUpListener(90, 0, keyTowerLeftUpListener);
// tower right
keyHandler.addKeyDownListener(88, 0, keyTowerRightDownListener);
keyHandler.addKeyUpListener(88, 0, keyTowerRightUpListener);

// fire 32
keyHandler.addKeyDownListener(32, 0, keyFireListener);

// backsight
keyHandler.addKeyDownListener(16, 0, keyBacksightDown);

function keyBacksightDown() {
	camera.position.z = (backsightDown) ? 8 : 0;
	camera.position.y = (backsightDown) ? 6 : 3;
	camera.rotation.x = (backsightDown) ? -0.3 : 0;
	ownTank.changeVisibility(backsightDown, true);

	if (backsightDown) {
		ownTank.tower.remove(backsight);
	}
	else {
		backsight = createBacksight(0.03, 0.1, 10);
		ownTank.tower.add(backsight);
	}

	backsightDown = !backsightDown;
}

function keyTowerLeftDownListener() {
	// ownTank.tower.rotation.y += Math.PI / 420;
	SOCKET_CONTROLLER.send("rotateTowerLeftDown", {});
}

function keyTowerLeftUpListener() {
	SOCKET_CONTROLLER.send("rotateTowerLeftUp", {});
}

function keyTowerRightDownListener() {
	// ownTank.tower.rotation.y -= Math.PI / 420;
	SOCKET_CONTROLLER.send("rotateTowerRightDown", {});
}

function keyTowerRightUpListener() {
	SOCKET_CONTROLLER.send("rotateTowerRightUp", {});
}

function keyTopDownListener() {
	SOCKET_CONTROLLER.send("forwardDown", {});
}

function keyTopUpListener() {
	SOCKET_CONTROLLER.send("forwardUp", {});
}

function keyBottomDownListener() {
	SOCKET_CONTROLLER.send("backDown", {});
}

function keyBottomUpListener() {
	SOCKET_CONTROLLER.send("backUp", {});
}

function keyLeftDownListener() {
	SOCKET_CONTROLLER.send("rotateLeftDown", {});
}

function keyLeftUpListener() {
	SOCKET_CONTROLLER.send("rotateLeftUp", {});
}

function keyRightDownListener() {
	SOCKET_CONTROLLER.send("rotateRightDown", {});
}

function keyRightUpListener() {
	SOCKET_CONTROLLER.send("rotateRightUp", {});
}

var intersectLine;
function getIntersectingObjects() {
	if (!ownTank) {
		return [];
	}
	var direction = ownTank.tower.direction(ownTank);
	var startPos = getTankBulletStartPosition();

	var ray = new THREE.Raycaster(startPos, direction);
	var rayIntersects = ray.intersectObjects(scene.children, true);

	//	showBulletIntersectLine();

	return rayIntersects;
}

function showBulletIntersectLine() {
	var startEndPos = getBulletStartEndPos(1000);

	scene.remove(intersectLine);
	intersectLine = createLine3d(startEndPos.s, startEndPos.e, LINE_BASIC_MATERIAL_BLACK);
	scene.add(intersectLine);
}

function getTankBulletStartPosition() {
	var startPos = ownTank.position.clone();
	startPos.y = 1;
	return startPos;
}

function showTanksLabels() {
	var objects = getIntersectingObjects();

	TANK_INFO_LABEL_CONTROLLER.hide();

	if (objects && objects[0]) {
		for (var i in objects) {
			var object = objects[i];
			if (object.object instanceof THREE.Line) {
				continue;
			}

			var target = findTankObjectByChield(object.object);

			if (target && target.sessionId != SESSION_ID) {
				// var xy = toScreenXY(target.position, camera, null);

				TANK_INFO_LABEL_CONTROLLER.show(target);
				break;
			}
		}
	}

	setTimeout(showTanksLabels, 500);
}

function create2DTextSprite(text, font, color) {
	var canvas = document.createElement('canvas');
	var context = canvas.getContext('2d');
	context.font = font;
	context.fillStyle = color;
	context.fillText(text, 0, 10);

	var texture = new THREE.Texture(canvas);
	texture.needsUpdate = true;

	var spriteMaterial = new THREE.SpriteMaterial({
		map: texture
	});

	var textSprite = new THREE.Sprite(spriteMaterial);
	textSprite.scale.set(1, 1, 1);

	return textSprite;
}

function getBulletStartEndPos(distance) {
	var direction = ownTank.tower.direction(ownTank);

	var tg = Math.atan(direction.z / direction.x);
	var rotCos = Math.cos(tg);
	var rotSin = Math.sin(tg);
	var signX = direction.x >= 0 ? 1 : -1;
	var signZ = direction.z >= 0 ? 1 : -1;

	var endX = signX * Math.abs(distance * rotCos) + ownTank.position.x;
	var endZ = signZ * Math.abs(distance * rotSin) + ownTank.position.z;

	var startX = ownTank.position.x + 6 * (signX * Math.abs(rotCos));
	var startZ = ownTank.position.z + 6 * (signZ * Math.abs(rotSin));

	return {
		s: new THREE.Vector3(startX, 1.8, startZ),
		e: new THREE.Vector3(endX, 1.8, endZ)
	};
}

function keyFireListener() {
	if (!ownTank.weapon.reloaded) {
		return;
	}
	ownTank.weapon.reloaded = false;

	var distance = 100;
	var direction = ownTank.tower.direction(ownTank);
	var startEndPos = getBulletStartEndPos(distance);

	var scale = 0.1;
	var bullet = bullet1.clone();
	bullet.name = "bullet";
	bullet.scale.set(scale, scale, scale);
	bullet.position.set(startEndPos.s.x, startEndPos.s.y, startEndPos.s.z);

	var ray = new THREE.Raycaster(startEndPos.s, direction);
	var rayIntersects = ray.intersectObjects(scene.children, true);

	for (var i in rayIntersects) {
		var object = rayIntersects[i];
		var target = findTankObjectByChield(object.object);

		if (target && target.sessionId != SESSION_ID) {
			var oldMaterial = object.object.material;
			object.object.material = MATERIAL_RED;

			SOCKET_CONTROLLER.send("fire", target.sessionId);

			function revertMaterial() {
				object.object.material = oldMaterial;
			}

			setTimeout(revertMaterial, 100);
			break;
		}
	}

	scene.add(bullet);

	var step = 5;
	var stepX = Math.abs(bullet.position.x - startEndPos.e.x) / distance * step;
	var stepZ = Math.abs(bullet.position.z - startEndPos.e.z) / distance * step;
	animateBullet(bullet, startEndPos.e.x, startEndPos.e.z, stepX, stepZ, 10, (bullet.position.x < startEndPos.e.x), (bullet.position.z < startEndPos.e.z), direction);

	animateBacksight(5, 1, (5 > 1));
	animateRecharge(1 / ownTank.weapon.shotFreq * 1000);
}

function findTankObjectByChield(obj) {
	if (!obj) {
		return null;
	}

	if (obj.sessionId) {
		return obj;
	}
	else {
		return findTankObjectByChield(obj.parent);
	}
}

function animateBullet(bullet, endX, endZ, stepX, stepZ, timeout, b1, b2, direction) {
	setTimeout(function() {

		if ((bullet.position.x <= endX) == b1) {
			if (b1)
				bullet.position.x += stepX;
			else
				bullet.position.x -= stepX;
		}
		if ((bullet.position.z <= endZ) == b2) {
			if (b2)
				bullet.position.z += stepZ;
			else
				bullet.position.z -= stepZ;
		}

		if (((bullet.position.z <= endZ) == b2) || ((bullet.position.x <= endX) == b1)) {
			animateBullet(bullet, endX, endZ, stepX, stepZ, timeout, b1, b2, direction);
		}
		else {
			scene.remove(bullet);
		}
	}, timeout);
}

function createBacksight(offset1, offset2, num) {
	var material = new THREE.LineBasicMaterial({
		color: 0x001100
	});

	var backsight = new THREE.Object3D();
	backsight.position.set(camera.position.x, camera.position.y - 0.05, camera.position.z - 2);

	var step = 2 * Math.PI / num;
	for (var i = 0; i < num; i++) {
		var x1 = Math.cos(i * step) * offset1;
		var x2 = Math.cos(i * step) * offset2;
		var y1 = Math.sin(i * step) * offset1;
		var y2 = Math.sin(i * step) * offset2;

		var geometry = new THREE.Geometry();
		geometry.vertices.push(new THREE.Vector3(x1, y1, 0));
		geometry.vertices.push(new THREE.Vector3(x2, y2, 0));

		var line = new THREE.Line(geometry, material);
		line.scale.x = line.scale.y = line.scale.z = 1;

		backsight.add(line);
	}

	return backsight;
}

function createRechargeLabel(offset1, offset2, maxLines, lines) {
	var material = (maxLines <= lines) ? LINE_BASIC_MATERIAL_GREEN : LINE_BASIC_MATERIAL_ORANGE;

	var rechargeLabel = new THREE.Object3D();
	rechargeLabel.position.set(0, 0, -1.5);

	var step = (Math.PI / maxLines / 2);
	var angleOffset = Math.PI / 1.5;

	for (var i = maxLines - 1; i > 0; i--) {
		if (maxLines - i > lines) {
			break;
		}

		var x1 = Math.cos(i * step + angleOffset) * offset1;
		var y1 = Math.sin(i * step + angleOffset) * offset1;
		var x2 = Math.cos(i * step + angleOffset) * offset2;
		var y2 = Math.sin(i * step + angleOffset) * offset2;

		var line = createLine3d(new THREE.Vector3(x1, y1, 0), new THREE.Vector3(x2, y2, 0), material);
		rechargeLabel.add(line);

		line = createLine3d(new THREE.Vector3(x1, y1 - 0.005, 0), new THREE.Vector3(x2, y2 - 0.005, 0), LINE_BASIC_MATERIAL_BLACK);
		rechargeLabel.add(line);
	}

	return rechargeLabel;
}

function createLine3d(startVec, endVec, material) {
	var geometry = new THREE.Geometry();
	geometry.vertices.push(startVec);
	geometry.vertices.push(endVec);

	var line = new THREE.Line(geometry, material);
	line.scale.x = line.scale.y = line.scale.z = 1;

	return line;
}

function animateBacksight(start, end, inc) {
	clearTimeout(this.timer);
	if (!backsightDown) {
		return;
	}

	ownTank.tower.remove(backsight);

	backsight = createBacksight(start * 0.03, start * 0.1, 10);
	ownTank.tower.add(backsight);

	if ((start > end) == inc) {
		start += (inc) ? -0.1 : 0.1;

		this.timer = setTimeout(function() {
			animateBacksight(start, end, inc);
		}, 300);
	}
}

function onConnect(tanks) {
	validateArray(tanks);

	addTanks(tanks);

	initTankObjects(tanks);
}

function addTanks(tanks) {
	tanks.forEach(addTank);
}

function addTank(tank) {
	TANKS_MANAGER.addTank(tank);
}

function bindCameraToTank(uuid) {
	if (cameraIsBind)
		return;

	var tank = TANKS_MANAGER.getTank(uuid);

	camera.lookAt(tank.tower.position);
	camera.position.set(0, 6, 8);
	camera.rotation.set(-Math.PI / 10, 0, 0);

	tank.tower.add(camera);

	ownTank = tank;
	$(".health-bar .cur").html(ownTank.health);
	$(".health-bar .max").html(ownTank.totalHealth);
	cameraIsBind = true;
}

/**
 * delay in seconds
 */
function animateRecharge(delay) {
	var passedTime = 0;
	var timer = null;
	var timerDelay = 60;

	animate();

	function animate() {
		passedTime += timerDelay;

		var perc = 100 / delay * passedTime;
		var lines = Math.round(30 / 100 * perc);

		camera.remove(rechargeLabel);

		rechargeLabel = createRechargeLabel(0.43, 0.45, 30, lines);
		camera.add(rechargeLabel);

		if (passedTime > delay) {
			clearTimeout(timer);
			passedTime = 0;
			ownTank.weapon.reloaded = true;
		}
		else {
			timer = setTimeout(function() {
				animate();
			}, timerDelay);
		}
	}
}

function initTankObjects(tanks) {
	tanks.forEach(function(tank) {
		scene.add(tank);
	});
}

function init() {
	// scene
	scene = new THREE.Scene();
//	scene.fog = new THREE.Fog(0xffffff, 1, 50);

	// camera
	camera = new THREE.PerspectiveCamera(50, window.innerWidth / window.innerHeight, 1, 10000);
	camera.rotation.z = 0;
	camera.rotation.x = -Math.PI / 5;
	camera.lookAt(scene.position);
	scene.add(camera);

	// Lights
	scene.add(AMBIENT_LIGHT);

	var light = new THREE.DirectionalLight(0x666666, 1.75);
	light.position.set(50, 30, 100);
	light.position.multiplyScalar(1.3);

	scene.add(light);

	// render
	renderer = new THREE.WebGLRenderer({
		antialias: true
	});
	renderer.setSize(window.innerWidth, window.innerHeight);
//	renderer.setClearColor(scene.fog.color);
	renderer.autoClear = false;

	document.body.appendChild(renderer.domElement);

	// statistic
	stats = new Stats();
	document.body.appendChild(stats.domElement);

	$("canvas").hide();

	loadObjects();

	loadLabels();

	showTanksLabels();

	RESOURCE_MANAGER.start();

	// Events
	window.addEventListener('resize', onWindowResize, false);

	ATMOSPHERE_MODEL.addListener("update_tank_position", onObjectLocationChange);
	ATMOSPHERE_MODEL.addListener("on_tower_rotate", onTowerRotate);
	ATMOSPHERE_MODEL.addListener("init_all_tanks", onInitAllTanks);
	ATMOSPHERE_MODEL.addListener("init_new_tank", onInitTank);
	ATMOSPHERE_MODEL.addListener("remove_tank", onDisconnectTank);
	ATMOSPHERE_MODEL.addListener("on_wounded", onWounded);
	ATMOSPHERE_MODEL.addListener("on_destroyed", onDestroyed);
}

/**
 * @param {{id:number,sessionId:String,health:number,dmg:number}} data
 */
function onWounded(data) {
	console.log("onWounded", data);
	validateInt(data.id);
	validateString(data.sessionId);
	validateFloat(data.health);
	validateFloat(data.dmg);

	var obj = scene.getObjectById(data.id, false);
	obj.health -= data.dmg;

	console.log("onWounded", data.sessionId == SESSION_ID);
	if (data.sessionId == SESSION_ID) {
		$(".health-bar .cur").html(data.health);

		scene.remove(AMBIENT_LIGHT);
		AMBIENT_LIGHT = new THREE.AmbientLight(0x660000);
		scene.add(AMBIENT_LIGHT);

		setTimeout(function() {
			scene.remove(AMBIENT_LIGHT);
			AMBIENT_LIGHT = new THREE.AmbientLight(0x666666);
			scene.add(AMBIENT_LIGHT);
		}, 100);
	}
}

function onDestroyed(data) {
	console.log("onDestroyed", data);
	validateInt(data.id);
	validateString(data.sessionId);
	validateFloat(data.health);

	var obj = scene.getObjectById(data.id, false);
	scene.remove(obj);
}

function loadLabels() {
	console.log("loadLabels");

	// add Recharge Label
	rechargeLabel = createRechargeLabel(0.43, 0.45, 30, 30);
	camera.add(rechargeLabel);
}

function onDisconnectTank(data) {
	console.warn("onDisconnectTank", data);
	var tank = TANKS_MANAGER.getTank(data.sessionId);
	if (!tank) {
		throw new Error("Tank not found");
	}

	scene.remove(tank);
	TANKS_MANAGER.remove(data.sessionId);
	// TODO delete from manager
}

function onInitAllTanks(data) {
	console.log("onInitAllTanks", data);

	var tanks = [];

	for (var i in data) {
		var obj = data[i];
		tanks.push(createTankObject(obj));
	}

	onConnect(tanks);

	bindCameraToTank(SESSION_ID);
}

function onInitTank(data) {
	console.log("onInitTank", data);
	var tank = createTankObject(data);

	var oldTank = TANKS_MANAGER.getTank(tank.sessionId);
	if (!oldTank) {
		addTank(tank);

		scene.add(tank);
	}

	bindCameraToTank(SESSION_ID);
}

/**
 *
 * @param {{pX:number,pY:number,pZ:number,rX:number,rY:number,rZ:number}}data
 * @returns {Tank}
 */
function createTankObject(data) {
	var position = new THREE.Vector3(data.pX, data.pY, data.pZ);
	var rotation = new THREE.Vector3(data.rX, data.rY, data.rZ);
	var tank = new Tank(data.sessionId, position, rotation, MATERIAL_BLUE, data.totalHealth, data.health, data.engine, data.suspension, data.tower, data.weapon, data.tankType);
	tank.id = data.id;
	return tank;
}

function onTowerRotate(data) {
	validateInt(data.id);
	validateFloat(data.ry);
	var obj = scene.getObjectById(data.id, false);
	if (obj) {
		var tower = obj.tower;
		if (tower) {
			tower.newRotation.push(new THREE.Vector3(0, data.ry, 0));
		}
		else {
			console.error("Tower not found for Object {0}".format(data.id));
		}
	}
}

function onObjectLocationChange(data) {
	validateInt(data.id);
	validateFloat(data.rx);
	validateFloat(data.ry);
	validateFloat(data.rz);
	validateFloat(data.px);
	validateFloat(data.py);
	validateFloat(data.pz);

	var obj = scene.getObjectById(data.id, false);
	if (obj) {
		obj.newPosition.push(new THREE.Vector3(data.px, data.py, data.pz));
		obj.newRotation.push(new THREE.Vector3(data.rx, data.ry, data.rz));
	}
	else {
		// console.error("Object {0} not found on scene".format(data.id));
	}
}

function onWindowResize() {
	camera.aspect = window.innerWidth / window.innerHeight;
	camera.updateProjectionMatrix();

	renderer.setSize(window.innerWidth, window.innerHeight);
}

function loadObjects() {
	// load interior
	var loader = new THREE.AssimpJSONLoader(LOADING_MANAGER);
	// loader.load(OBJECTS_PATH + 'interior_3ds.json', callbackLoadInterior);

	loadInteriorResources();

	// load
	bullet1 = new THREE.Mesh(new THREE.CubeGeometry(2, 2, 2), new THREE.MeshNormalMaterial());

	// load tanks
	loadTankResources();

	loadSceneCube();
}

function loadSceneCube() {
	var path = "/resources/media/images/textures/cube/skybox/";
	var format = '.jpg';
	var urls = [
		path + 'px' + format, path + 'nx' + format,
		path + 'py' + format, path + 'ny' + format,
		path + 'pz' + format, path + 'nz' + format
	];

	var textureCube = THREE.ImageUtils.loadTextureCube(urls, new THREE.CubeRefractionMapping());

	var shader = THREE.ShaderLib[ "cube" ];
	shader.uniforms[ "tCube" ].value = textureCube;

	var material = new THREE.ShaderMaterial({
		fragmentShader: shader.fragmentShader,
		vertexShader: shader.vertexShader,
		uniforms: shader.uniforms,
		depthWrite: false,
		side: THREE.BackSide

	});

	mesh = new THREE.Mesh(new THREE.BoxGeometry(1000, 1000, 1000), material);
	scene.add(mesh);
}

function loadTankResources() {
	TANK_OBJ_SET.forEach(function(id) {
		RESOURCE_MANAGER.add(function() {
			OBJ_MTL_LOADER.load(OBJECTS_PATH + id + ".obj", OBJECTS_PATH + id + ".mtl", callbackLoadTankResources);
		});
	});
}

function callbackLoadTankResources(tank) {
	console.log("callbackLoadTankResources", tank);
	RESOURCE_MANAGER.loaded();

	TANK_MANAGER.push(tank);
}

function loadInteriorResources() {
	RESOURCE_MANAGER.add(function() {
		var path = OBJECTS_PATH + "grasslight-big.jpg";
		THREE.ImageUtils.loadTexture(path, undefined, callbackLoadInteriorTexture);
	});
}

function callbackLoadInteriorTexture(texture) {
	console.log("callbackLoadInteriorTexture", texture);

	RESOURCE_MANAGER.loaded();

	var material = new THREE.MeshPhongMaterial({
		map: texture
	});
	texture.wrapS = texture.wrapT = THREE.RepeatWrapping;
	texture.repeat.set(1600, 1600);
	texture.anisotropy = 16;

	var mesh = new THREE.Mesh(new THREE.PlaneGeometry(20000, 20000), material);
	mesh.rotation.x = -Math.PI / 2;
	mesh.receiveShadow = true;

	scene.add(mesh);

	OBJ_MTL_LOADER.load(OBJECTS_PATH + "MI-8_1" + ".obj", OBJECTS_PATH + "MI-8_1" + ".mtl", function(object) {
		object.scale.set(30, 30, 30);
		object.position.set(-20, 0, -20);

		scene.add(object);

		var o2 = object.clone();
		o2.position.set(10, 0, -20);
		scene.add(o2);

		var o3 = object.clone();
		o3.position.set(40, 0, -20);
		scene.add(o3);

		var o4 = object.clone();
		o4.position.set(70, 0, -20);
		scene.add(o4);
	});
}

function onAllResourceLoaded() {
	console.log("onAllResourceLoaded");

	$("canvas").show();

	OBJ_TANK = TANK_MANAGER[TANK_TYPE];

	SOCKET_CONTROLLER.wsConnect();
}

function callbackLoadInterior(object) {
	object.scale.x = object.scale.y = object.scale.z = INTERIOR_SCALE;
	object.updateMatrix();

	scene.add(object);
}

function animate() {
	requestAnimationFrame(animate);

	render();
}

function render() {
	renderer.render(scene, camera);

	stats.update();
}

function Tank(sessionId, position, rotation, texture, totalHealth, health, engine, suspension, tower, weapon, tankType) {
	validateString(sessionId);
	validateInstance(position, THREE.Vector3);
	validateInstance(rotation, THREE.Vector3);
	validateInstance(texture, THREE.MeshBasicMaterial);
	validateInt(totalHealth);
	validateInt(health);
	validateFloat(engine.forwardSpeed);
	validateFloat(engine.backSpeed);
	validateFloat(weapon.shotFreq);
	validateInt(tankType);

	var obj = TANK_MANAGER[tankType].clone();
	obj.sessionId = sessionId;
	obj.position.set(position.x, position.y, position.z);
	obj.rotation.set(rotation.x, rotation.y, rotation.z);
	obj.positionLock = false;
	obj.newPosition = [];
	obj.rotationLock = false;
	obj.newRotation = [];
	obj.texture = texture;
	obj.totalHealth = totalHealth;
	obj.health = health;
	obj.engine = engine;
	obj.suspension = suspension;
	obj.forwardSpeed = engine.forwardSpeed;
	obj.backSpeed = engine.backSpeed;
	obj.rotateSpeed = suspension.rotateSpeed;
	obj.moveTimeout = 0;

	obj.weapon = {};
	obj.weapon.reloaded = true;
	obj.weapon.shotFreq = weapon.shotFreq;

	obj.tower = obj.getObjectByName("Gun_tower");
	obj.tower.rotationLock = false;
	obj.tower.newRotation = [];
	obj.tower.rotation.set(0, tower.rY, 0);

	obj.toString = function() {
		return "Tank[sessionId:{0},position:{1},rotation:{2},texture:{3},health:{4}]".format(sessionId, position, rotation, texture, health);
	};

	TASK_MANAGER.addTask(obj);
	return obj;
}

function TankManager() {
	var tankMap = [];

	this.addTank = function(tank) {
		validateInstance(tank, THREE.Object3D);

		tankMap[tank.sessionId] = tank;
	};
	this.getTank = function(sessionId) {
		validateString(sessionId);

		return tankMap[sessionId];
	};

	this.remove = function(sessionId) {
		delete tankMap[sessionId];
	};
}

/**
 *
 * @param {THREE.Vector3} obj
 * @param {number} speed
 */
function changePosition(obj, speed) {
	if (obj.newPosition && !obj.positionLock) {
		var newPosition = obj.newPosition.shift();
		if (newPosition) {
			var bX = obj.position.x <= newPosition.x;
			var bY = obj.position.y <= newPosition.y;
			var bZ = obj.position.z <= newPosition.z;

			var diffX = Math.abs(obj.position.x - newPosition.x);
			var diffY = Math.abs(obj.position.y - newPosition.y);
			var diffZ = Math.abs(obj.position.z - newPosition.z);

			if (!(diffX == 0 && diffZ == 0)) {
				var diffMax = Math.max(diffX, diffY, diffZ);
				var count = (diffMax / speed);
				// console.log("count", count, speed, diffMax);

				var stepX = (diffX == 0) ? 0 : diffX / count;
				var stepY = (diffY == 0) ? 0 : diffY / count;
				var stepZ = (diffZ == 0) ? 0 : diffZ / count;
				// console.log("step ", stepX, stepY, stepZ);

				obj.position.x += (bX) ? stepX : -stepX;
				obj.position.y += (bY) ? stepY : -stepY;
				obj.position.z += (bZ) ? stepZ : -stepZ;

				if (obj.position.x < newPosition.x != bX && obj.position.y < newPosition.y != bY && obj.position.z < newPosition.z != bZ) {
					obj.position.x = newPosition.x;
					obj.position.y = newPosition.y;
					obj.position.z = newPosition.z;
				}
			}
		}
	}
}

/**
 *
 * @param {THREE.Vector3} obj
 * @param {number} speed
 */
function changeRotation(obj, speed) {
	if (obj.newRotation && !obj.rotationLock) {
		var newRotation = obj.newRotation.shift();
		if (newRotation) {
			obj.rotationLock = true;
			obj.rotateTo(newRotation.x, newRotation.y, newRotation.z, speed);
		}
	}
}

/**
 *
 * @param {THREE.Vector3} obj
 * @param {number} speed
 */
function changeTowerRotation(obj, speed) {
	var tower = obj.tower;
	if (tower) {
		changeRotation(tower, speed);
	}
	else {
		console.error("Tower not found");
	}
}

function TaskManager() {
	var tasks = [];

	this.addTask = function(obj) {
		validateInt(obj.id);

		tasks[obj.id] = obj;
	};

	run();
	function run() {

		for (var i in tasks) {
			var task = tasks[i];
			changePosition(task, 1);
			changeRotation(task, 0.1);
			changeTowerRotation(task, 0.1)
		}

		setTimeout(run, 5);
	}
}

function TankInfoLabelController() {
	var that = this;
	var targetNameLabel;
	var targetHealthLabel;

	this.show = function(tank) {
		// remove old label
		that.hide();

		// create target name label
		targetNameLabel = create2DTextSprite(tank.id, "14px Arial", "#000000");
		targetNameLabel.position.set(0.4, -0.20, -2);

		// create target health label
		var healthPerc = 100 / tank.totalHealth * tank.health;
		targetHealthLabel = create2DTextSprite(healthPerc + "%", "Bold 12px Arial", "#000000");
		targetHealthLabel.position.set(0.46, -0.3, -2);

		camera.add(targetNameLabel);
		camera.add(targetHealthLabel);
	};

	this.hide = function() {
		camera.remove(targetNameLabel);
		camera.remove(targetHealthLabel);
	};
}