
package Font.classes;

public class Encriptacion{


    public Encriptacion() {
       
    }

    //Pre: La funcion recibe una contraseña
    //Post: El sistema devuelve la contraseña permutada
    public String permutar_password(String ps){
           
        char[] caracteres = ps.toCharArray();
        int n = caracteres.length;

        for (int i = 0; i < n / 2; i++) {
            char temp = caracteres[i];
            caracteres[i] = caracteres[n - 1 - i];
            caracteres[n - 1 - i] = temp;
        }
        for (int i = 0; i < n; i++) {
        caracteres[i] = (char) (caracteres[i] + 10);
        }
        return new String(caracteres);
        }
         
    //Pre: La funcion recibe una contraseña permutada
    //Post: El sistema devuelve la contraseña sin la permutación
    public String despermutar_password(String us){

            char[] caracteres = us.toCharArray();
            int n = caracteres.length;
            for (int i = 0; i < n; i++) {
            caracteres[i] = (char) (caracteres[i] - 10);
            }
            for (int i = 0; i < n / 2; i++) {
                char temp = caracteres[i];
                caracteres[i] = caracteres[n - 1 - i];
                caracteres[n - 1 - i] = temp;
            }
            return new String(caracteres);
        }
}