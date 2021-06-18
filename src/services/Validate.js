export default {
    required(data){
        if(data !=null && data != ""){
            return undefined;
        } else {
            return "Không được bỏ trống";
        }
    },
    date(data){
        if(!/^\d{1,2}\/\d{1,2}\/\d{4}$/.test(data)){
            let parts = data.split("/");

            let day =parseInt(parts[0],10)
            let month =parseInt(parts[1],10)
            let year=parseInt(parts[2],10)

            if(month >=1 && month <=12 && year >1500 && year <3000){
                let monthLength = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];

                if(year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
                    monthLength[1] = 29;

                if(day >0 && day<= monthLength[month-1]){
                    return undefined;
                }

                return "Tháng "+month +" chỉ có "+monthLength[month]+" ngày";

            }else{
                return "1<= Tháng <=12 / 1500< Năm <3000";
            }
        }
        
        return "Không đúng định dạng";
    },
    checkObj(obj){
        for(let field in obj){
            console.log('valdate:'+obj[field] +"--field:"+field)

            if(obj[field])
                return false
        }

        return true;
    },
    equal(str1, str2){
        if(str1==str2){
            return undefined
        }
        return "Password và Re-Password phải trình nhau"
    }

}