class Util {
    static getCommonColors() {
        return ["#EF5350", "#42A5F5", "#FFA726", "#93c756", "#EC407A", "#AB47BC", "#7E57C2", "#5C6BC0", "#26C6DA", "#26A69A", "#D4E157", "#FFEE58", "#FFCA28"];
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
        return "#999999";
    }

    static getLightGrey() {
        return "#cccccc";
    }

    static getFormatDuration(timeMillis) {
        if (timeMillis >= 1000 * 60) {
            return Math.floor(timeMillis / (1000 * 60)) + "m" + Math.floor((timeMillis % (1000 * 60)) / 1000) + "s";
        }
        if (timeMillis >= 1000) {
            return (Math.floor(timeMillis) / 1000).toFixed(3) + "s";
        }
        return Math.floor(timeMillis) + "ms";
    }

    static getFormatMAndSAndMS(timeMillis) {
        return new Date(timeMillis).toLocaleTimeString();
    }

    /**
     * @param methodInfo
     * @returns {*}
     */
    static getColorForMethod(methodInfo) {
        const charCode = methodInfo.methodName.charCodeAt(methodInfo.methodName.length - 1);
        if (charCode >= 48 && charCode < 50) {
            return Util.getCommonColors()[0];
        }
        if (charCode >= 50 && charCode < 57) {
            return Util.getCommonColors()[1];
        }
        if (charCode >= 65 && charCode < 72) {
            return Util.getCommonColors()[2];
        }
        if (charCode >= 72 && charCode <= 81) {
            return Util.getCommonColors()[3];
        }
        if (charCode >= 81 && charCode <= 91) {
            return Util.getCommonColors()[4];
        }
        if (charCode >= 97 && charCode < 100) {
            return Util.getCommonColors()[3];
        }
        if (charCode >= 100 && charCode < 105) {
            return Util.getCommonColors()[4];
        }
        if (charCode >= 105 && charCode < 109) {
            return Util.getCommonColors()[3];
        }
        if (charCode >= 109 && charCode < 112) {
            return Util.getCommonColors()[4];
        }
        if (charCode >= 112 && charCode < 114) {
            return Util.getCommonColors()[3];
        }
        if (charCode >= 114 && charCode < 116) {
            return Util.getCommonColors()[4];
        }
        if (charCode >= 116 && charCode < 118) {
            return Util.getCommonColors()[3];
        }
        if (charCode >= 118 && charCode < 122) {
            return Util.getCommonColors()[4];
        }
        return Util.getGrey();
    }
}

export default Util;
