function extend(sub, source) {
    for (var property in source) {
        if (sub[property] && (typeof (sub[property]) == 'object')
            && (sub[property].toString() == '[object Object]')
            && source[property])
            extend(sub[property], source[property]);
        else
            sub[property] = source[property];
    }
    return sub;
}

if (!String.prototype.trim) {
    String.prototype.trim = function () {
        return this.replace(/^\s+|\s+$/g, '');
    };
}
if (!String.prototype.replaceAll) {
    String.prototype.replaceAll = function (search, replace) {
        return this.split(search).join(replace);
    };
}
if (!String.prototype.format) {
    String.prototype.format = function () {
        var args = arguments;
        return this.replace(/{(\d+)}/g, function (match, number) {
            return typeof args[number] != 'undefined' ? args[number] : match;
        });
    };
}

function isString(obj) {
    return Object.prototype.toString.call(obj) == '[object String]';
}

function isFunction(functionToCheck) {
    return functionToCheck
        && {}.toString.call(functionToCheck) === '[object Function]';
}

function isArray(someVar) {
    return Object.prototype.toString.call(someVar) === '[object Array]';
}

function isObject(someVar) {
    return Object.prototype.toString.call(someVar) === '[object Object]';
}

function isBoolean(someVar) {
    return Object.prototype.toString.call(someVar) === '[object Boolean]';
}

function isEmpty(input) {
    return (input + "").trim().length === 0;
}

/**
 * @param {string|number} input
 * @returns {boolean}
 */
function isNumeric(input) {
    return (input - 0) == input && !isEmpty(input);
}

/**
 *
 * @param {string|number} input
 * @returns {boolean}
 */
function isInteger(input) {
    input = (input + "").trim();
    var res = (input - 0) == input && !isEmpty(input);
    res = res && input.indexOf(".") == -1;
    res = res && input.indexOf(",") == -1;

    return res;
}

/**
 *
 * @param {string|number} input
 * @returns {boolean}
 */
function isFloat(input) {
    input = ((input + "").trim()).replaceAll(",", ".");
    return (input - 0) == input && !isEmpty(input);
}

// Validations

function validateInstance(v, i) {
    if (!(v instanceof i)) {
        throw new Error("{0} should be an instanceof {1}".format(v, i));
    }
}

function validateInt(v) {
    if (!isInteger(v)) {
        throw new Error("{0} should be an Integer".format(v));
    }
}
function validateFloat(v) {
    if (!isFloat(v)) {
        console.log("v:", v, isFloat(v))
        throw new Error("{0} should be an Float".format(v));
    }
}

function validateString(v) {
    if (!isString(v)) {
        throw new Error("{0} should be a String".format(v));
    }
}

function validateArray(v) {
    if (!isArray(v)) {
        throw new Error("{0} should be an Array".format(v));
    }
}