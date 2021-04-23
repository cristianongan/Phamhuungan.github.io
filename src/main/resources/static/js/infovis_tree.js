var labelType, useGradients, nativeTextSupport, animate;

(function() {
    var ua = navigator.userAgent,
    iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
    typeOfCanvas = typeof HTMLCanvasElement,
    nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
    textSupport = nativeCanvasSupport
    && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
    //I'm setting this based on the fact that ExCanvas provides text support for IE
    //and that as of today iPhone/iPad current text support is lame
    labelType = (!nativeCanvasSupport || (textSupport && !iStuff))? 'Native' : 'HTML';
    nativeTextSupport = labelType == 'Native';
    useGradients = nativeCanvasSupport;
    animate = !(iStuff || !nativeCanvasSupport);
})();

var st_child;
var st_ref;

function addNode(newTextNode) {
    var newNodeJSON = jq.evalJSON(newTextNode);
    st_child.addSubtree(newNodeJSON, "animate", {
        hideLabels: false,
        onComplete: function() {
            console.log("subtree added");
        }
    });
}

function refresh(treeData) {
    st_child.loadJSON(jq.evalJSON(treeData));
    //compute node positions and layout
    st_child.compute();
    //optional: make a translation of the tree
    st_child.geom.translate(new $jit.Complex(-200, 0), "current");
    //Emulate a click on the root node. 
    st_child.onClick(st_child.root);
//end
}

function refreshReference(treeData) {
    st_ref.loadJSON(jq.evalJSON(treeData));
    //compute node positions and layout
    st_ref.compute();
    //optional: make a translation of the tree
    st_ref.geom.translate(new $jit.Complex(-200, 0), "current");
    //Emulate a click on the root node.
    st_ref.onClick(st_ref.root);
//end
}

function switchPosition(orient, id) {
    st_child.switchPosition(orient, "animate", {
        onComplete: function() {
            zAu.send(new zk.Event(zk.Widget.$('$'+id), "onChangeComplete","", {
                    toServer:true
                }));
        }
    });
}

function switchAlignment(orient, id) {
    st_child.switchAlignment(orient, "animate", {
        onComplete: function() {
            zAu.send(new zk.Event(zk.Widget.$('$'+id), "onChangeComplete","", {
                    toServer:true
                }));
        }
    });
}

function initChildren(treeData, id){
    //init Spacetree
    //Create a new ST instance
    var wrapper = '' + zk.Widget.$("$"+id).$n().id;
    
    st_child = new $jit.ST({
        'injectInto': wrapper,
        orientation: 'top',
        levelDistance: 50,
        duration: 400,
        //sibling and subtrees offsets
        siblingOffset: 1,
        subtreeOffset: 1,
        //add styles/shapes/colors
        //to nodes and edges
        overridable : true,
        //enable panning
        Navigation: {
          enable:true,
          panning:true,
          zooming: 10 //zoom speed. higher is more sensible
        },
        //set overridable=true if you want
        //to set styles for nodes individually 
        Node: {
            overridable: true,
            width: 80,
            height: 80,
            color: '#B8B8B8',
            type: 'icon'
        },

        Edge: {
            type: 'line',
            overridable: true,
            lineWidth:2
        },

//        Events: {
//            enable: true,
//            type: 'HTML',
//            //Hien bang chi tiet doi tuong khi click chuot phai
//            onRightClick: function(node){
//                if(!node) {return};
//
//                zAu.send(new zk.Event(zk.Widget.$('$'+id), "onShowDetail", node.id, {
//                    toServer:true
//                }));
//            }
//        },
        //change the animation/transition effect
        transition: $jit.Trans.Quart.easeOut,
        
        onBeforeCompute: function(node){
            console.log("loading " + node.name);
        },
        
        onAfterCompute: function(node){
            console.log("done");
        },

        //This method is triggered on label
        //creation. This means that for each node
        //this method is triggered only once.
        //This method is useful for adding event
        //handlers to each node label.
        onCreateLabel: function(label, node){
            //add some styles to the node label
            var style = label.style;
            label.id = node.id;
            style.color = '#333333';
            style.fontSize = '11px';
            style.textAlign = 'center';
            style.width = "80px";
            style.height = "40px";
            style.cursor = 'pointer';
            style.fontWeight = "bold";
            style.overflow = "hidden";
            style.paddingTop = '70px';
            label.innerHTML = node.name;
            //Delete the specified subtree 
            //when clicking on a label.
            //Only apply this method for nodes
            //in the first level of the tree.
            label.onclick = function(){
            	st_child.onClick(node.id);

                zAu.send(new zk.Event(zk.Widget.$('$'+id), "onClickNode", label.id, {
                    toServer:true
                }));
            };

//            label.ondblclick = function(){
//                zAu.send(new zk.Event(zk.Widget.$('$'+id), "onShowDetail", label.id, {
//                    toServer:true
//                }));
//            };

        },
        
        onBeforePlotNode: function(node){
            //add some color to the nodes in the path between the
            //root node and the selected node.
            if (node.selected) {
                node.data.$color = "#FF7FF7";
            }
            else {
                delete node.data.$color;
                //if the node belongs to the last plotted level
                if(!node.anySubnode("exist")) {
                    //count children number
                    var count = 0;
                    node.eachSubnode(function(n) {
                        count++;
                    });
                    //assign a node color based on
                    //how many children it has
                    node.data.$color = [
                        '#AAAAAA',
                        '#BAABAA',
                        '#CAACAA', 
                        '#DAADAA',
                        '#EAAEAA',
                        '#FAAFAA'
                    ][count];
                }
            }

            if( node.getData('type') === 'icon' ){
                var img = new Image();

                img.height = "48px";
                img.width = "48px";

                img.addEventListener('load', function(){
                    node.setData('image',img); // store this image object in node
                }, false);

                img.src = "data:image" + normalizeJSON(node.data.imgSrc);
            }
        },

        //This method is called right before plotting
        //an edge. It's useful for changing an individual edge
        //style properties before plotting it.
        //Edge data proprties prefixed with a dollar sign will
        //override the Edge global style properties.
        onBeforePlotLine: function(adj){
            if (adj.nodeFrom.selected && adj.nodeTo.selected) {
                adj.data.$color = "#D16D16";
                adj.data.$lineWidth = 4;
            }
            else {
                delete adj.data.$color;
                delete adj.data.$lineWidth;
            }
        }
    });
    //load json data
    //    var mynewjson = zk.Widget.$('$data').getValue();
    //    st_child.loadJSON(jq.evalJSON(mynewjson));
    st_child.loadJSON(jq.evalJSON(treeData));
    //compute node positions and layout
    st_child.compute();
    //optional: make a translation of the tree
    st_child.geom.translate(new $jit.Complex(-200, 0), "current");
    //Emulate a click on the root node.
    st_child.onClick(st_child.root);
//end
}

function initReference(treeData, id){
    //init Spacetree
    //Create a new ST instance
    var wrapper = '' + zk.Widget.$("$"+id).$n().id;

    st_ref = new $jit.ST({
        'injectInto': wrapper,
        orientation: 'top',
        levelDistance: 50,
        duration: 400,
        //sibling and subtrees offsets
        siblingOffset: 1,
        subtreeOffset: 1,
        //add styles/shapes/colors
        //to nodes and edges
        overridable : true,
        //enable panning
        Navigation: {
          enable:true,
          panning:true,
          zooming: 10 //zoom speed. higher is more sensible
        },
        //set overridable=true if you want
        //to set styles for nodes individually
        Node: {
            overridable: true,
            width: 80,
            height: 80,
            color: '#B8B8B8',
            type: 'icon'
        },

        Edge: {
            type: 'line',
            overridable: true,
            lineWidth:2
        },

//        Events: {
//            enable: true,
//            type: 'HTML',
//            //Hien bang chi tiet doi tuong khi click chuot phai
//            onRightClick: function(node){
//                if(!node) {return};
//
//                zAu.send(new zk.Event(zk.Widget.$('$'+id), "onShowDetail", node.id, {
//                    toServer:true
//                }));
//            }
//        },
        //change the animation/transition effect
        transition: $jit.Trans.Quart.easeOut,

        onBeforeCompute: function(node){
            console.log("loading " + node.name);
        },

        onAfterCompute: function(node){
            console.log("done");
        },

        //This method is triggered on label
        //creation. This means that for each node
        //this method is triggered only once.
        //This method is useful for adding event
        //handlers to each node label.
        onCreateLabel: function(label, node){
            //add some styles to the node label
            var style = label.style;
            label.id = node.id;
            style.color = '#333333';
            style.fontSize = '11px';
            style.textAlign = 'center';
            style.width = "80px";
            style.height = "40px";
            style.cursor = 'pointer';
            style.fontWeight = "bold";
            style.overflow = "hidden";
            style.paddingTop = '70px';
            label.innerHTML = node.name;
            //Delete the specified subtree
            //when clicking on a label.
            //Only apply this method for nodes
            //in the first level of the tree.
            label.onclick = function(){
            	st_ref.onClick(node.id);

                zAu.send(new zk.Event(zk.Widget.$('$'+id), "onClickNode", label.id, {
                    toServer:true
                }));
            };

//            label.ondblclick = function(){
//                zAu.send(new zk.Event(zk.Widget.$('$'+id), "onShowDetail", label.id, {
//                    toServer:true
//                }));
//            };
        },

        onBeforePlotNode: function(node){
            //add some color to the nodes in the path between the
            //root node and the selected node.
            if (node.selected) {
                node.data.$color = "#FF7FF7";
            }
            else {
                delete node.data.$color;
                //if the node belongs to the last plotted level
                if(!node.anySubnode("exist")) {
                    //count children number
                    var count = 0;
                    node.eachSubnode(function(n) {
                        count++;
                    });
                    //assign a node color based on
                    //how many children it has
                    node.data.$color = [
                        '#AAAAAA',
                        '#BAABAA',
                        '#CAACAA',
                        '#DAADAA',
                        '#EAAEAA',
                        '#FAAFAA'
                    ][count];
                }
            }

            if( node.getData('type') === 'icon' ){
                var img = new Image();

                img.height = "48px";
                img.width = "48px";

                img.addEventListener('load', function(){
                    node.setData('image',img); // store this image object in node
                }, false);

                img.src = "data:image" + normalizeJSON(node.data.imgSrc);
                
            }
        },

        //This method is called right before plotting
        //an edge. It's useful for changing an individual edge
        //style properties before plotting it.
        //Edge data proprties prefixed with a dollar sign will
        //override the Edge global style properties.
        onBeforePlotLine: function(adj){
            if (adj.nodeFrom.selected && adj.nodeTo.selected) {
                adj.data.$color = "#D16D16";
                adj.data.$lineWidth = 4;
            }
            else {
                delete adj.data.$color;
                delete adj.data.$lineWidth;
            }
        }
    });
    //load json data
    //    var mynewjson = zk.Widget.$('$data').getValue();
    //    st_child.loadJSON(jq.evalJSON(mynewjson));
    st_ref.loadJSON(jq.evalJSON(treeData));
    //compute node positions and layout
    st_ref.compute();
    //optional: make a translation of the tree
    st_ref.geom.translate(new $jit.Complex(-200, 0), "current");
    //Emulate a click on the root node.
    st_ref.onClick(st_ref.root);
//end
}

function normalizeJSON(json){
    if(json==null|| json.lenght<=0){
        return "";
    }

    return json.replace(/(\r\n|\r|\n)/g, '\\r\n');
}