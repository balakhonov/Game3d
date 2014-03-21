//THREE.Object3D.prototype.moveTo = function (x, y, z, speed) {
//	validateFloat(x);
//	validateFloat(y);
//	validateFloat(z);
//	validateFloat(speed);
//
//	var diffX = x - this.position.x;
//	var diffZ = z - this.position.z;
//
//	if (!(diffX == 0 && diffZ == 0)) {
//		var angle = Math.atan2(diffX, diffZ);
//		var stepX = Math.sin(angle) * (speed);
//		var stepZ = Math.cos(angle) * (speed);
//
//		this.position.x += stepX;
//		this.position.z += stepZ;
//
//		if (Math.abs(this.position.z - z) > speed / 2) {
//			console.log("move");
//			this.moveTo(x, y, z, speed);
//		} else {
//			this.position.x = x;
//			this.position.z = z;
//			this.positionLock = false;
//		}
//	} else {
//		this.positionLock = false;
//	}
//};


/**
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} speed - points/ms
 */
//THREE.Object3D.prototype.moveTo = function (x, y, z, speed) {
//	validateFloat(x);
//	validateFloat(y);
//	validateFloat(z);
//	validateFloat(speed);
//
//	var obj = this;
//
//	var TIMEOUT = 1;
//
//	var bX = obj.position.x <= x;
//	var bY = obj.position.y <= y;
//	var bZ = obj.position.z <= z;
//
//	var diffX = Math.abs(obj.position.x - x);
//	var diffY = Math.abs(obj.position.y - y);
//	var diffZ = Math.abs(obj.position.z - z);
//	var diffMax = Math.max(diffX, diffY, diffZ);
//	var count = diffMax / (speed/1000 * TIMEOUT);
//	console.log("count", count, speed, diffMax);
//
//	var stepX = (diffX == 0) ? 0 : diffX / count;
//	var stepY = (diffY == 0) ? 0 : diffY / count;
//	var stepZ = (diffZ == 0) ? 0 : diffZ / count;
////	console.log("xyz ", x, y, z);
////	console.log("XYZ ", obj.position.x, obj.position.y, obj.position.z);
//	console.log("step ", stepX, stepY, stepZ);
//
////	clearTimeout(obj.moveTimeout);
//	move();
//
//	function move() {
//		console.log("move");
//		obj.position.x += (bX) ? stepX : -stepX;
//		obj.position.y += (bY) ? stepY : -stepY;
//		obj.position.z += (bZ) ? stepZ : -stepZ;
//
//		diffX = Math.abs(obj.position.x - x);
//		diffY = Math.abs(obj.position.y - y);
//		diffZ = Math.abs(obj.position.z - z);
//
//		// console.log(obj.position.x < x, obj.position.y < y, obj.position.z <
//		// z);
//
////		console.log("XYZ ", obj.position.x, obj.position.y, obj.position.z);
//		if (!(obj.position.x < x != bX && obj.position.y < y != bY
//			&& obj.position.z < z != bZ)) {
//			obj.moveTimeout = setTimeout(move, TIMEOUT);
//		} else {
//			obj.position.x = x;
//			obj.position.y = y;
//			obj.position.z = z;
//			obj.positionLock = false;
//		}
//	}
//};

THREE.Object3D.prototype.direction = function (relativeObject) {
	var direction = new THREE.Vector3(0, 0, -1).applyQuaternion(this.quaternion);
	if(relativeObject){
		direction.x =  -Math.sin(this.rotation.y + relativeObject.rotation.y);
		direction.z =  -Math.cos(this.rotation.y + relativeObject.rotation.y);
	}
	return direction;
};

/**
 * @param {number} x
 * @param {number}   y
 * @param {number}    z
 * @param {number}   speed - points/ms
 */
THREE.Object3D.prototype.rotateTo = function (x, y, z, speed) {
	validateFloat(x);
	validateFloat(y);
	validateFloat(z);
	validateFloat(speed);

	var obj = this;

	var TIMEOUT = 100;

//	console.log("XYZ ", obj.position.x, obj.position.y, obj.position.z);
//	console.log("xyz ", x, y, z);

	var bX = obj.rotation.x <= x;
	var bY = obj.rotation.y <= y;
	var bZ = obj.rotation.z <= z;

	var diffX = Math.abs(obj.rotation.x - x);
	var diffY = Math.abs(obj.rotation.y - y);
	var diffZ = Math.abs(obj.rotation.z - z);
	var diffMax = Math.max(diffX, diffY, diffZ);
	var count = diffMax / (speed * TIMEOUT);

	var stepX = (diffX == 0) ? 0 : diffX / count;
	var stepY = (diffY == 0) ? 0 : diffY / count;
	var stepZ = (diffZ == 0) ? 0 : diffZ / count;

//	clearTimeout(obj.moveRTimeout);
	move();

	function move() {
		obj.rotation.x += (bX) ? stepX : -stepX;
		obj.rotation.y += (bY) ? stepY : -stepY;
		obj.rotation.z += (bZ) ? stepZ : -stepZ;

		diffX = Math.abs(obj.rotation.x - x);
		diffY = Math.abs(obj.rotation.y - y);
		diffZ = Math.abs(obj.rotation.z - z);

		// console.log(obj.rotation.x < x, obj.rotation.y < y, obj.rotation.z <
		// z);

		if (obj.rotation.x < x == bX || obj.rotation.y < y == bY
			|| obj.rotation.z < z == bZ) {
			obj.moveRTimeout = setTimeout(move, TIMEOUT);
		} else {
			obj.rotation.x = x;
			obj.rotation.y = y;
			obj.rotation.z = z;
			obj.rotationLock = false;
		}
	}
};

THREE.Object3D.prototype.changeVisibility = function( visible, recursively ) {
    this.visible = visible;
    if(recursively){
        for(var i = 0,il = this.children.length;i<il;i++){
            this.children[i].changeVisibility(visible,recursively);
        }
    }
};