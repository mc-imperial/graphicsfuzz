var globalObject = null;
var globalCounter = 0;
var webgl_context;

function stop() {
  if(globalObject != null) {
    console.log("Stopping previous render loop.")
    var g = globalObject;
    globalObject = null;
    g.running = false;
    g.gl.clear(g.gl.COLOR_BUFFER_BIT);
  }
}

function startSafe() {
  try {
    document.getElementById("console").value = "";
    start();
  } catch(e) {
    document.getElementById("console").value = e;
  }
}

function start() {
  var fragmentShaderSource = document.getElementById("shader_source").value;
  var uniformsInfoSource = document.getElementById("uniforms_info").value;

  var vertexShaderSource = "";
  switch (webgl_context) {
  case "experimental-webgl": // WebGL 1
    vertexShaderSource =
      "#version 100\n" +
      "attribute vec2 aVertexPosition;\n" +
      "void main(void) { gl_Position = vec4(aVertexPosition, 0.0, 1.0); }";
    break;
  case undefined:
  case "webgl2":            // WebGL 2
    vertexShaderSource =
      "#version 300 es\n" +
      "in vec2 aVertexPosition;\n" +
      "void main(void) { gl_Position = vec4(aVertexPosition, 0.0, 1.0); }";
    break;
  }

  var specification = "{ \"glstate\": [ {" +
    "\"width\": 256," +
    "\"height\": 256," +
    "\"program\": {" +
    "  \"vs\": \"" + vertexShaderSource.replace(/\n/g, "\\n") + "\"," +
    "  \"fs\": \"" + fragmentShaderSource.replace(/\n/g, "\\n") + "\" }," +
    "\"buffer\": { \"data\": [ -1.0,1.0,-1.0,-1.0,1.0,-1.0,1.0,1.0 ] }" +
    "}, {" +
    "\"uniform\": {" + uniformsInfoSource + "}," +
    "\"vertex\": {" + 
    "  \"aVertexPosition\": {" +
        "\"enabled\": \"true\", \"size\": 2, \"stride\": 8, \"offset\": 0" +
    "  } } } ] }";
    

  stop();
  console.log("Start");

  var canvas = document.getElementById("opengl-canvas");
  var gl = null;
  try {
    gl = canvas.getContext("webgl2");
  }
  catch(e) {
    console.log(e);
  }
  if (!gl) {
    throw "Failed to initialize WebGL.";
  }

  globalObject = { running : true, id : globalCounter, gl : gl };
  globalCounter += 1;
  var globalObjectRef = globalObject;

  render(canvas, specification);
}

