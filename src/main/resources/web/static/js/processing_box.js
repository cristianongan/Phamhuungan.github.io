/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


zUtl.progressbox = function(id, msg, mask, icon) {
    mask=true;
    
    if (mask && zk.Page.contained.length) {
        for (var c = zk.Page.contained.length, e = zk.Page.contained[--c]; e; e = zk.Page.contained[--c]) {
            if (!e._applyMask)
                e._applyMask = new zk.eff.Mask({
                    id: e.uuid + "-mask",
                    message: msg,
                    anchor: e.$n()
                });
        }
        return;
    }

    if (mask)
        zk.isBusy++;

    var x = jq.innerX(), y = jq.innerY(),
    style = ' style="left:'+x+'px;top:'+y+'px"',
    idtxt = id + '-t',
    idmsk = id + '-m',
    html = '<div id="'+id+'"';
    if (mask)
        html += '><div id="' + idmsk + '" class="z-modal-mask"'+style+'></div';
    html += '><div id="'+idtxt+'" class="z-loading"'+style
    +'><div class="z-loading-indicator"><span class="z-loading-icon"></span> '
    +msg+'</div></div>';
    if (icon)
        html += '<div class="' + icon + '"></div>';
    jq(document.body).append(html + '</div>');
    var $n = jq(id, zk),
    n = $n[0],
    $txt = jq(idtxt, zk);
    if (mask)
        n.z_mask = new zk.eff.FullMask({
            mask: jq(idmsk, zk)[0],
            zIndex: $txt.css('z-index') - 1
        });

    //center
    var txt = $txt[0],
    st = txt.style;
    st.left = jq.px((jq.innerWidth() - txt.offsetWidth) / 2 + x);
    st.top = jq.px((jq.innerHeight() - txt.offsetHeight) / 2 + y);

    $n.zk.cleanVisibility();
}