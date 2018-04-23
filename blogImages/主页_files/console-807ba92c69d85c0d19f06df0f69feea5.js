/**
 * Created by guoxing on 2017/3/2.
 */


function addZero(i) {
    if(i<10){
        i='0'+i;
    }else{
        i=''+i+''
    }
    return i;
}

function laterMinutes(val) {
    var result;
    if(val<15){
        result='15';
    }else if(val<30){
        result='30';
    }else if(val<45){
        result='45';
    }else if(val<59){
        result='59';
    }else{
        result='00';
    }
    return result;
}

function beforeMinutes(val) {
    var result;
    if(val<15){
        result='00';
    }else if(val<30){
        result='15';
    }else if(val<45){
        result='30';
    }else if(val<59){
        result='45';
    }else{
        result='59';
    }
    return result;
}

//日期格式為2017-01-01
function caculatedDays(startDate,endDate) {
    var startTime = new Date(Date.parse(startDate.replace(/-/g,   "/"))).getTime();
    var endTime = new Date(Date.parse(endDate.replace(/-/g,   "/"))).getTime();
    var dates = Math.abs((startTime - endTime))/(1000*60*60*24);
    return  dates+1;
}

// 日期格式為149081924239
function caculateDays(startDate,endDate) {
    var dates = Math.ceil(Math.abs((startDate - endDate))/(1000*60*60*24));
    return  dates;
}

function addDate(val) {
    var result;
    result=parseInt(val)+1;
    // addZero(result);
    return addZero(result);
}
function subDate(val) {
    var result;
    result=parseInt(val)-1;
    // addZero(result);
    return addZero(result);
}

//menu
function adMenuManage(first,second) {
    $('.main-sidebar').find('a').removeClass('sidenav-selected');
    $('.'+first+'').addClass('sidenav-selected');

    $('.child-nav-list-group').find('.list-group-item').removeClass('active');
    $('.child-nav-list-group').find('.'+second+'').addClass('active');
}

//getTime
function adGetTime() {
    var ad_resuleTime = new Date().getTime();
    return ad_resuleTime;
}

//daterangebtn position change
function daterangebtnChange() {
    var lenLi=$('.daterangepicker .ranges ul li').length;
    if(lenLi>7){
        $('.daterangepicker .ranges .range_inputs').insertAfter('.daterangepicker .calendar.right');
        $('.daterangepicker .range_inputs').addClass('adBottomBtn');
    }
    /*$('.daterangepicker').each(function () {
        var lenLi = $(this).find(".ranges ul li").length;
        if(lenLi > 7){
            $(this).find('.ranges .range_inputs').insertAfter('this .calendar.right');
            $(this).find('.range_inputs').addClass('adBottomBtn');
        }
    })*/
}

//event place
function adEvent(event,val_x,val_y) {
    return {
        pageX:event.pageX-event.offsetX-val_x,
        pageY:event.pageY-event.offsetY-val_y
    };
}