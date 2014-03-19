if (!SESSION_ID) {
	throw new Error("SESSION_ID not found!");
}
if (!TANK_OBJ_SET) {
	throw new Error("TANK_OBJ_SET not found!");
}
console.info("SESSION_ID:", SESSION_ID);
console.info("TANK_OBJ_SET:", TANK_OBJ_SET);

var w = window;
var camera, scene, renderer, canvas;
var geometry, material, mesh;

var TANK_MANAGER = [];

var OBJ_TANK;

var OBJECTS_PATH = "/resources/media/js/obj/";

var OBJ_MTL_LOADER = new THREE.OBJMTLLoader();

init();
animate();

function init() {
	canvas = document.getElementById("canvas");

	// scene
	scene = new THREE.Scene();
	scene.fog = new THREE.Fog(0xcce0ff, 0.1, 150);

	// camera
	camera = new THREE.PerspectiveCamera(70, canvas.clientWidth / canvas.clientHeight, 1, 1000);
	camera.position.y = 4;
	camera.position.z = -5;
	camera.rotation.y = Math.PI / 2;

	scene.add(camera);
	camera.lookAt(scene);

	//	Lights
	scene.add(new THREE.AmbientLight(0x666666));

	var light = new THREE.DirectionalLight(0x666666, 1.75);
	light.position.set(50, 30, 100);
	light.position.multiplyScalar(1.3);
	scene.add(light);

	// loaders
	loadObjects();

	// render
	renderer = new THREE.WebGLRenderer({ antialias: true });
	renderer.setSize(canvas.clientWidth, canvas.clientHeight);
	renderer.setClearColor(scene.fog.color);
	console.log(canvas);
	canvas.appendChild(renderer.domElement);

	// Events
	window.addEventListener('resize', onWindowResize, false);
	window.addEventListener('mousedown', mousedown, false);

	function mousedown(e) {
		var left = e.pageX;

		function mousemove(event) {
			if (left < event.pageX) {
				OBJ_TANK.rotation.y += 0.1;
			} else {
				OBJ_TANK.rotation.y -= 0.1;
			}
			left = event.pageX;
		}

		function mouseup() {
			document.removeEventListener('mousemove', mousemove);
			document.removeEventListener('mouseup', mouseup);
		}

		document.addEventListener('mousemove', mousemove, false);
		document.addEventListener('mouseup', mouseup, false);
	}
}

function onWindowResize() {
	camera.aspect = canvas.clientWidth / canvas.clientHeight;
	camera.updateProjectionMatrix();

	renderer.setSize(canvas.clientWidth, canvas.clientHeight);
}

function animate() {
	requestAnimationFrame(animate);
	render();
}

function render() {
	renderer.render(scene, camera);
}


function loadObjects() {
	// ground
	var initColor = new THREE.Color(0x497f13);
	var initTexture = THREE.ImageUtils.generateDataTexture(1, 1, initColor);

	var groundMaterial = new THREE.MeshPhongMaterial({ color: 0xffffff, specular: 0x111111, map: initTexture });

	var groundTexture = THREE.ImageUtils.loadTexture(OBJECTS_PATH + "grasslight-big.jpg", undefined, function () {
		groundMaterial.map = groundTexture;
	});
	groundTexture.wrapS = groundTexture.wrapT = THREE.RepeatWrapping;
	groundTexture.repeat.set(1600, 1600);
	groundTexture.anisotropy = 16;

	var mesh = new THREE.Mesh(new THREE.PlaneGeometry(20000, 20000), groundMaterial);
	mesh.rotation.x = -Math.PI / 2;
	mesh.receiveShadow = true;
	scene.add(mesh);

	TANK_OBJ_SET.forEach(function (id) {

		function onLoad(object) {
			var index = TANK_MANAGER.length;

			// add tank item to tanks list
			$('.tank-list').append('<li data="{0}"><img src="/resources/media/js/obj/{1}.png" style="width: 100px;" class="img-rounded"/></li>'.format(index, id));

			// add tank to manager
			TANK_MANAGER.push(object);
			if (TANK_MANAGER.length == TANK_OBJ_SET.length) {
				onTankSelect(0)
			}
		}

		OBJ_MTL_LOADER.load(OBJECTS_PATH + id + ".obj", OBJECTS_PATH + id + ".mtl", onLoad);
	});

	function onTankSelect(index) {
		var tank = TANK_MANAGER[index];
		TANK_MANAGER.forEach(function (obj) {
			scene.remove(obj);
		});
		scene.add(tank);
		camera.lookAt(tank.position);
		OBJ_TANK = tank;

		$("input[name=tankType]").val(index);
	}

	$('.tank-list').on("click", "li", function (el) {
		var index = $(el.target).parent().attr("data");
		onTankSelect(index);
	});
}

