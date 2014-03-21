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
var camera, scene, renderer;
var geometry, material, mesh;

var ownTank;
var OBJ_TANK;
var bullet1;

var OBJECTS_PATH = "/resources/media/js/obj/";
var INTERIOR_SCALE = 1;

var userUUID = SESSION_ID;
var userList = [];
var userObject;
var TANK_MANAGER = [];

var backsightDown = false;
var backsight = new THREE.Object3D();

var RESOURCE_MANAGER = new THREE.LoadingManager();
RESOURCE_MANAGER.onProgress = function(item, loaded, total) {
	console.log(item, loaded, total);
};
RESOURCE_MANAGER.onLoad = function(item, loaded, total) {
	console.log("RESOURCE_MANAGER onLoad");

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

var ATMOSPHERE_MODEL = new AtmosphereModel();
var TASK_MANAGER = new TaskManager();

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
keyHandler.addKeyDownListener(90, 10, keyTowerLeftDownListener);
keyHandler.addKeyUpListener(90, 10, keyTowerLeftUpListener);
// tower right
keyHandler.addKeyDownListener(88, 10, keyTowerRightDownListener);
keyHandler.addKeyUpListener(88, 10, keyTowerRightUpListener);

// fire 32
keyHandler.addKeyDownListener(32, 0, keyFireListener);

// backsight
keyHandler.addKeyDownListener(16, 0, keyBacksightDown);

function keyBacksightDown() {
	console.log("keyBacksightDown", camera.position, camera.rotation, ownTank);
	camera.position.z = (backsightDown) ? 8 : 0;
	camera.position.y = (backsightDown) ? 6 : 3;
	camera.rotation.x = (backsightDown) ? -0.3 : 0;
	ownTank.changeVisibility(backsightDown, true);

	if (backsightDown) {
		ownTank.tower.remove(backsight);
	} else {
		backsight = createBacksight(0.03, 0.1, 10);
		ownTank.tower.add(backsight);
	}

	backsightDown = !backsightDown;
}

function keyTowerLeftDownListener() {
	ownTank.tower.rotation.y += Math.PI / 420;
}

function keyTowerLeftUpListener() {

}

function keyTowerRightDownListener() {
	ownTank.tower.rotation.y -= Math.PI / 420;
}

function keyTowerRightUpListener() {

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

function keyFireListener() {
	var distance = 100;

	var direction = ownTank.tower.direction(ownTank);
	var tg = Math.atan(direction.z / direction.x);
	var rotCos = Math.cos(tg);
	var rotSin = Math.sin(tg);
	var signX = direction.x >= 0 ? 1 : -1;
	var signZ = direction.z >= 0 ? 1 : -1;

	var endX = signX * Math.abs(distance * rotCos) + ownTank.position.x;
	var endZ = signZ * Math.abs(distance * rotSin) + ownTank.position.z;

	var startX = -5 * (signX * Math.abs(rotCos));
	var startZ = -5 * (signZ * Math.abs(rotSin));

	var scale = 0.1;
	var bullet = bullet1.clone();
	bullet.name = "bullet";
	bullet.scale.set(scale, scale, scale);
	bullet.position.copy(ownTank.position);
	bullet.position.sub(new THREE.Vector3(startX, -1.8, startZ));

	// var ray = new THREE.Raycaster(bullet.position, direction);

	// var rayIntersects = ray.intersectObjects(scene.children, true);
	// rayIntersects.forEach(function (object) {
	// object.object.material = MATERIAL_RED;
	// console.log(object.object);
	// setTimeout(function () {
	// object.object.material = MATERIAL_GREEN;
	// }, 100);
	// });

	scene.add(bullet);

	var step = 5;
	var stepX = Math.abs(bullet.position.x - endX) / distance * step;
	var stepZ = Math.abs(bullet.position.z - endZ) / distance * step;
	animateBullet(bullet, endX, endZ, stepX, stepZ, 10,
			(bullet.position.x < endX), (bullet.position.z < endZ), direction);

	animateBacksight(5, 1, (5 > 1));
}

function animateBullet(bullet, endX, endZ, stepX, stepZ, timeout, b1, b2,
		direction) {
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

		var ray = new THREE.Raycaster(bullet.position, direction);

		var rayIntersects = ray.intersectObjects(scene.children, true);
		if (rayIntersects[0]) {
			var object = rayIntersects[0];
			if (object.object.name != "bullet" && object.distance < 6) {
				// console.log(object.object.parent.parent.health);
				object.object.parent.parent.health -= 30;

				object.object.material = MATERIAL_RED;
				setTimeout(function() {
					if (object.object.parent.parent.health < 0) {
						scene.remove(object.object.parent.parent);
					} else {
						object.object.material = MATERIAL_GREEN;
					}
				}, 100);

				scene.remove(bullet);
				return;
			}
		}

		if (((bullet.position.z <= endZ) == b2)
				|| ((bullet.position.x <= endX) == b1)) {
			animateBullet(bullet, endX, endZ, stepX, stepZ, timeout, b1, b2,
					direction);
		} else {
			scene.remove(bullet);
		}
	}, timeout);
}

function createBacksight(offset1, offset2, num) {
	var material = new THREE.LineBasicMaterial({
		color: 0x001100
	});

	var backsight = new THREE.Object3D();
	backsight.position.set(camera.position.x, camera.position.y - 0.05,
			camera.position.z - 2);

	var step = 2 * Math.PI / num;
	for ( var i = 0; i < num; i++) {
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

function animateBacksight(start, end, inc) {
	clearTimeout(this.timer);
	if(!backsightDown){
		return;
	}

	ownTank.tower.remove(backsight);
	
	backsight = createBacksight(start * 0.03, start * 0.1, 10);
	ownTank.tower.add(backsight);

	if ((start > end) == inc) {
		start += (inc) ? -0.1 : 0.1;

		this.timer = setTimeout(function() {
			animateBacksight(start, end, inc);
		}, 10);
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
	var tank = TANKS_MANAGER.getTank(uuid);

	camera.lookAt(tank.tower.position);
	camera.position.set(0, 6, 8);
	camera.rotation.set(-Math.PI / 10, 0, 0);

	tank.tower.add(camera);

	ownTank = tank;
}

function initTankObjects(tanks) {
	tanks.forEach(function(tank) {
		scene.add(tank);
	});
}

function onNewUserConnect(uuid) {
	if (!isString(uuid)) {
		throw new Error("uuid should be ans String", uuid);
	}
	console.info("onNewUserConnect", uuid);

	userList.push(uuid);
	// addObject(uuid);
}

function onReceiveMessage(message) {
	if (!isObject(message)) {
		throw new Error("Message sould be an object type");
	}
	console.info("onReceiveMessage", message);
}

function addObject(uuid) {
	if (!isString(uuid)) {
		throw new Error("uuid should be ans String", uuid);
	}
	console.info("addObject", uuid);

	var loader = new THREE.ObjectLoader(RESOURCE_MANAGER);
	loader.load('/resources/media/js/obj/1.json', function(object) {
		var texture = THREE.ImageUtils
				.loadTexture("/resources/media/js/obj/421795696.png");
		var material = new THREE.MeshLambertMaterial({
			map: texture
		});

		object.material = material;

		scene.add(object);

		if (uuid == userUUID) {
			userObject = object;
		}
	});
}

function init() {
	// scene
	scene = new THREE.Scene();
	scene.fog = new THREE.Fog(0xcce0ff, 0.1, 150);

	// camera
	camera = new THREE.PerspectiveCamera(50, window.innerWidth
			/ window.innerHeight, 1, 10000);
	// camera.position.set(0, 8, 8);
	camera.rotation.z = 0;
	camera.rotation.x = -Math.PI / 5;
	camera.lookAt(scene.position);
	scene.add(camera);

	// Lights
	scene.add(new THREE.AmbientLight(0x666666));

	var light = new THREE.DirectionalLight(0x666666, 1.75);
	light.position.set(50, 30, 100);
	light.position.multiplyScalar(1.3);

	scene.add(light);

	// render
	renderer = new THREE.WebGLRenderer({
		antialias: true
	});
	renderer.setSize(window.innerWidth, window.innerHeight);
	renderer.setClearColor(scene.fog.color);

	document.body.appendChild(renderer.domElement);

	// loaders
	loadObjects();

	// Events
	window.addEventListener('resize', onWindowResize, false);

	ATMOSPHERE_MODEL
			.addListener("update_tank_position", onObjectLocationChange);
	ATMOSPHERE_MODEL.addListener("init_all_tanks", onInitAllTanks);
	ATMOSPHERE_MODEL.addListener("init_new_tank", onInitTank);
	ATMOSPHERE_MODEL.addListener("remove_tank", onDisconnectTank);
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

	for ( var i in data) {
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

function createTankObject(data) {
	var position = new THREE.Vector3(data.pX, data.pY, data.pZ);
	var rotation = new THREE.Vector3(data.rX, data.rY, data.rZ);
	var tank = new Tank(data.sessionId, position, rotation, MATERIAL_BLUE,
			data.health, data.forwardSpeed, data.backSpeed, data.rotateSpeed,
			data.tankType);
	tank.id = data.id;
	return tank;
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
	} else {
		// console.error("Object {0} not found on scene".format(data.id));
	}
}

function onWindowResize() {
	renderer.setSize(window.innerWidth, window.innerHeight);
	camera.aspect = window.innerWidth / window.innerHeight;
	camera.updateProjectionMatrix();
}

function loadObjects() {
	// load interior
	var loader = new THREE.AssimpJSONLoader(RESOURCE_MANAGER);
	// loader.load(OBJECTS_PATH + 'interior_3ds.json', callbackLoadInterior);

	// ground
	var initColor = new THREE.Color(0x497f13);
	var initTexture = THREE.ImageUtils.generateDataTexture(1, 1, initColor);

	var groundMaterial = new THREE.MeshPhongMaterial({
		color: 0xffffff,
		specular: 0x111111,
		map: initTexture
	});

	var groundTexture = THREE.ImageUtils.loadTexture(OBJECTS_PATH
			+ "grasslight-big.jpg", undefined, function() {
		groundMaterial.map = groundTexture;
	});
	groundTexture.wrapS = groundTexture.wrapT = THREE.RepeatWrapping;
	groundTexture.repeat.set(1600, 1600);
	groundTexture.anisotropy = 16;

	var mesh = new THREE.Mesh(new THREE.PlaneGeometry(20000, 20000),
			groundMaterial);
	mesh.rotation.x = -Math.PI / 2;
	mesh.receiveShadow = true;
	scene.add(mesh);

	bullet1 = new THREE.Mesh(new THREE.CubeGeometry(2, 2, 2),
			new THREE.MeshNormalMaterial());

	TANK_OBJ_SET.forEach(function(id) {

		function onLoad(object) {
			TANK_MANAGER.push(object);

			if (TANK_MANAGER.length == TANK_OBJ_SET.length) {
				OBJ_TANK = TANK_MANAGER[TANK_TYPE];

				onAllResourceLoaded();
			}
		}

		OBJ_MTL_LOADER.load(OBJECTS_PATH + id + ".obj", OBJECTS_PATH + id
				+ ".mtl", onLoad);
	});
}

function onAllResourceLoaded() {
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
}

function Tank(sessionId, position, rotation, texture, health, forwardSpeed,
		backSpeed, rotateSpeed, tankType) {
	validateString(sessionId);
	validateInstance(position, THREE.Vector3);
	validateInstance(rotation, THREE.Vector3);
	validateInstance(texture, THREE.MeshBasicMaterial);
	validateInt(health);
	validateFloat(forwardSpeed);
	validateFloat(backSpeed);
	validateFloat(rotateSpeed);
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
	obj.health = health;
	obj.forwardSpeed = forwardSpeed;
	obj.backSpeed = backSpeed;
	obj.rotateSpeed = rotateSpeed;
	obj.moveTimeout = 0;

	obj.tower = obj.getObjectByName("Gun_tower");

	obj.toString = function() {
		return "Tank[sessionId:{0},position:{1},rotation:{2},texture:{3},health:{4}]"
				.format(sessionId, position, rotation, texture, health);
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
 * @param {THREE.Vector3}
 *            obj
 * @param {number}
 *            speed
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

				if (obj.position.x < newPosition.x != bX
						&& obj.position.y < newPosition.y != bY
						&& obj.position.z < newPosition.z != bZ) {
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
 * @param {THREE.Vector3}
 *            obj
 * @param {number}
 *            speed
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

function TaskManager() {
	var tasks = [];

	this.addTask = function(obj) {
		validateInt(obj.id);

		tasks[obj.id] = obj;
	};

	run();
	function run() {

		for ( var i in tasks) {
			var task = tasks[i];
			changePosition(task, 1);
			changeRotation(task, 0.1)
		}

		setTimeout(run, 1);
	}
}
