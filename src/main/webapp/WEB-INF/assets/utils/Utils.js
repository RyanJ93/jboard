'use strict';

class Utils {
    static getDayName(day){
        let dayName = '';
        switch ( day ){
            case 1: {
                dayName = 'Mon';
            }break;
            case 2: {
                dayName = 'Tue';
            }break;
            case 3: {
                dayName = 'Wed';
            }break;
            case 4: {
                dayName = 'Thu';
            }break;
            case 5: {
                dayName = 'Fri';
            }break;
        }
        return dayName;
    }
}

export default Utils;
