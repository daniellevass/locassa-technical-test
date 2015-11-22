package locassatechnicaltest.danielle.dev.com.technicaltest;

/**
 * Created by daniellevass on 21/11/2015.
 */
public class WeatherResponse {


    private Query query;

    public Query getQuery() {
        return query;
    }

    public class Query
    {

        private Results results;

        public Results getResults() {
            return results;
        }
    }


    public class Results
    {
        private Channel channel;

        public Channel getChannel() {
            return channel;
        }
    }

    public class Channel{
        private Item item;

        public Item getItem() {
            return item;
        }
    }

    public class Item{
        private String title;
        private Condition condition;

        public String getTitle() {
            return title;
        }

        public Condition getCondition() {
            return condition;
        }

        public String getShortDescription(){

            StringBuilder sb = new StringBuilder();

            sb.append(title);

            sb.append("\n");

            sb.append(condition.getText() + " - " + condition.getTemp() + "\u00B0C");


            return sb.toString();

        }
    }

    public class Condition
    {
        private String code;
        private String temp;
        private String text;

        public String getCode() {
            return code;
        }

        public String getTemp() {
            return temp;
        }

        public String getText() {
            return text;
        }


        public int getBackgroundColourBasedOnTemp(){

            //convert the temp
            int temperature = Integer.parseInt(temp);

            int colour;

            if (temperature < 0){
                colour =  R.color.md_blue_700;
            } else if (temperature < 10){
                colour =  R.color.md_light_blue_500;
            } else if (temperature < 20){
                colour =  R.color.md_yellow_500;
            } else if (temperature < 30){
                colour =  R.color.md_orange_500;
            }else  {
                colour =  R.color.md_red_500;
            }


            return colour;
        }

    }

}
