#include <Servo.h>

/** DEFINE GLOBAL VARIABLES**/
String input; //PHONE INPUT STRING
char command; //USED FOR INTERPRETING PHONE INPUT
boolean servo = false;  //FALSE = WHEELY BAR OUT, TRUE = WHEELY BAR IN

Servo myservo; //SERVO OBJECT FOR CONTROLLING WHEELY BAR

/** DEFINE THE MOTORS WE NEED**/
/**DEFINE MOTOR PINS**/
//LEFT SIDE
int AIN1 = 7;   //CONTROLS LEFT SIDE DIRECTION
int AIN2 = 8;
int PWMA = 6;   //CONTROLS LEFT SIDE SPEED

//RIGHT SIDE
int BIN1 = 2;   //CONTROLS RIGHT SIDE DIRECTION
int BIN2 = 4;
int PWMB = 5;   //CONTROLS RIGHT SIDE SPEED

//SERVO
int servoPin = 9;

/** ASSERT INITIAL CONDITIONS **/
void setup() {
  /** ASSERT MOTOR SETTINGS **/
  pinMode(AIN2, OUTPUT); 
  pinMode(AIN1, OUTPUT); 
  pinMode(PWMA, OUTPUT); 
  pinMode(BIN1, OUTPUT);  
  pinMode(BIN2, OUTPUT);  
  pinMode(PWMB, OUTPUT); 
  myservo.attach(servoPin);  //SERVO CONTROLLED BY PIN 9
  myservo.write(170);   //SET WHEELY BAR TO OUT POSITION
  Serial.begin(9600); //Initialise communication for Bluetooth
}

/** THIS LOOPS WHILE BOARD HAS POWER **/
void loop() {
  input="";   //set input to blank for new input
  while(Serial.available() > 0) //loops through until no commands being send from phone app
  {
    command = ((byte)Serial.read());
    if(command==':')  //termination character sent at end of every command from phone
    {
      String commandLead = input.substring(0, 2); //first two characters determine vehicle side and direction
      int commandTail = (input.substring(2)).toInt(); //last characters determine speed
      if(commandLead == "SV")
      {
        if(servo == true)
        {
          myservo.write(170);
        }
        else
        {
          myservo.write(30);
        }
        servo = !servo;
      } 
      else if(commandLead == "BL")  //BACK LEFT
      {
        //set right side to forward
        digitalWrite(BIN1, HIGH);
        digitalWrite(BIN2, LOW);
        //set speed
        analogWrite(PWMB, commandTail);
      }
      else if(commandLead == "FL")  //FORWARD LEFT
      {
        //move right side backward
        digitalWrite(BIN2, HIGH);
        digitalWrite(BIN1, LOW);
        //set speed
        analogWrite(PWMB, commandTail);
      }
      else if(commandLead == "BR")  //BACK RIGHT
      {
        //move left side forward
        digitalWrite(AIN1, HIGH);
        digitalWrite(AIN2, LOW);
        //set speed
        analogWrite(PWMA, commandTail);
      }
      else if(commandLead == "FR")  //FORWARD RIGHT
      {
        //move left side backward
        digitalWrite(AIN1, LOW);
        digitalWrite(AIN2, HIGH);
        //set speed
        analogWrite(PWMA, commandTail);
      }
      break;
    }
    else {  //command is not terminated so get next character
      String temp = String(command);  //get command sent character
      input += temp;  //concatonate new character to input string
    }
    delay(1);   //found this necessary to prevent processor delays/overloading
  }
}
 
