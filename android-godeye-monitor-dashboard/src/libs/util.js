class Util {
    static getCommonColors() {
        return ["#EB4334", "#4586F3", "#FBBD06", "#93c756", "#999999"];
    }


    static getRed() {


        return Util.getCommonColors()[0];
    }

    static getBlue() {
        return Util.getCommonColors()[1];
    }

    static getOrange() {
        return Util.getCommonColors()[2];
    }

    static getGreen() {
        return Util.getCommonColors()[3];
    }

    static getGrey() {
        return Util.getCommonColors()[4];
    }

    static getLightGrey() {
        return "#cccccc";
    }

    static getFormatDuration(timeNanos) {
        if (timeNanos >= 1000000000 * 60) {
            return Math.floor(timeNanos / (1000000000 * 60)) + "m" + Math.floor((timeNanos % (1000000000 * 60)) / 1000000000) + "s";
        }
        if (timeNanos >= 1000000000) {
            return (timeNanos / 1000000000).toFixed(3) + "s";
        }
        if (timeNanos >= 1000000) {
            return (timeNanos / 1000000).toFixed(3) + "ms";
        }
        if (timeNanos >= 1000) {
            return (timeNanos / 1000).toFixed(3) + "us";
        }
        return Math.floor(timeNanos) + "ns";
    }
}

export default Util;
