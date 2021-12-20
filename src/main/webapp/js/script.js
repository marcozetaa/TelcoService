function excludeBox() {
    var group_=(el,callback)=>{
    el.forEach((checkbox)=>{
        callback(checkbox)
        })
    }

    group_(document.getElementsByName('check'),(item)=>{
        item.onclick=(e)=>{
            group_(document.getElementsByName('check'),(item)=>{
                item.checked=false;
            })
            e.target.checked=true;
            }
        })
}