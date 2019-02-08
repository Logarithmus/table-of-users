function toggleCheckboxes(master) {
    let boxes = document.getElementsByName("checkbox");
    boxes.forEach(box => {box.checked = master.checked});
}