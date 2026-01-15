public class Questions {
        String ques;
        String [] opt = new String[4];
        String ans;
        public Questions(String ques, String[] opt, String ans){
            this.ques = ques;
            for(int i= 0; i<4; i++){
                this.opt[i] = opt[i];
            }
            this.ans = ans;
        }
}