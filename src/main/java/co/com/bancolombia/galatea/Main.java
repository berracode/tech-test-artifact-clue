package co.com.bancolombia.galatea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {


        String[] manuscript10_1 = {
                "KBMPTVL", "GYEGTZWG", "URIOEOZT", "QRDDDES", "ERCNKNE", "YRLSSSSEF", "FOTECCCC", "XFTIBNH", "MGCFQKQW", "AOKPSMD"
        };

        String[] manuscript10_2 = {
                "UFBOCSUUK", "PUPPJMHM", "YSUGTKJ", "RUKUKKMM", "SYSHEIEE", "DQTVNIGLV", "ZFMEOI", "TCMSRIPR", "XWUJXAY", "FSMAZZZZ"
        };

        String[] manuscript10_3 = {
                "JRPYTP", "EIAYQQQQN", "HFXMYX", "QHKRAT", "VFHPHHHH", "WFWHUF", "XXXXFIWS", "UUYKAUAN", "GFDDSEJQG", "OAPXRHCJ"
        };


        String[] manuscript30_1 = {
                "HZREBBQO", "LKPPHNHC", "OVOSZZZZQ", "EVQPYRN", "XMWVFSX", "XETCPUI", "HARCMKAX", "NYVHSGC", "FZRICIV", "AAAACR",
                "HNWGOUGT", "UEMQZFW", "XRYQPEPCP", "UIUQUUCJ", "DEIQXWDOS", "CCCCMAI", "DMDIQWC", "CEKCTTY", "KLQQDB", "CBIWWUFH",
                "ELQGNR", "LTVBFLY", "HFECXM", "IGMACCNCU", "JKDVDCSB", "HMIUOPCH", "RKORNM", "PBXKCADV", "OLLLLQXOZ", "MLMJWJN"
        };

        String[] manuscript30_2 = {
                "CBYZLE", "LLLLDZI", "ECEJFCMV", "USBXOATG", "PSWUIB", "QCIVMMRHP", "GEVVVVD", "LBFNDKJVM", "DLRVON", "HILCMXGG",
                "ZFOLVBBUA", "SQIMLY", "PCJPRMEZ", "GONSGVUP", "KZZZZEAT", "JCMORS", "VTRNBUKL", "VSQQQQ", "RHWFNS", "TPQZHCP",
                "FIAVDRUI", "DJXELR", "PTYLBVQP", "UUUUBLDI", "TKIWBBUH", "BNTZBZ", "TZQYZRYW", "APYHENIMI", "PGPECBQ", "YKKEOQ"
        };

        String[] manuscript30_3 = {
                "WYTALS", "NAESGKJM", "MYUQPVDJ", "MPPPPN", "FHDOENNF", "UIHMMMMN", "WTQUGVSTX", "AUOXWR", "KLDOJMSWS", "GSFQNDRNE",
                "EJUQRX", "VXELHL", "JNUFUIXAC", "THUILR", "JAUDRUVR", "CJUNAX", "TVJDSYF", "APWJUR", "TSAAAAUJR", "JTIKEIAB",
                "KKKKLYBS", "YHPPCHKS", "XNMGVKTXD", "KFZGHCDCR", "QLKWWUWE", "TZQZGMV", "PTBFLYD", "EPNFCK", "GQCNGOSSX", "WIVLWHXUO"
        };


        String[] manuscript = {"RTHGQW", "XRLORE", "NARURR", "REVRAL", "EGSILE", "BRINDS"};

        containsArtifactClue(manuscript10_1);

    }

    private static boolean containsArtifactClue(String[] manuscript) {

        //TODO: generar matrices con lineas del mismo tamaño
        generateMatrices(manuscript);

        return false;
    }

    private static void generateMatrices(String[] manuscript) {
        var groupedManuscriptByLen = Arrays.stream(manuscript).collect(Collectors.groupingBy(String::length));
        List<Matrix> matrixList = new ArrayList<>();

        for (List<String> group : groupedManuscriptByLen.values()) {
            int rows = group.size();
            int cols = group.getFirst().length();

            var matrix = new Matrix(rows, cols);

            for (int i = 0; i < rows; i++) {
                var fullRow = group.get(i);

                for (int j = 0; j < cols; j++) {
                    var item = fullRow.substring(j, j + 1);
                    matrix.set(item, i, j);
                }
            }
            var text = String.format("Matrix [%s] [%s]", rows, cols);
            System.out.println(text);
            matrix.print();
            matrixList.add(matrix);
        }


    }

}