import moment from 'moment';

export default {
    formatDate(value){
        if (value) {
            return moment(String(value)).format('DD-MM-YYYY')
        }
    }
}