#include <AccelStepper.h>
#define HALFSTEP 8

// motor pins
#define motorPin1  2     // IN1 on the ULN2003 driver 1
#define motorPin2  3     // IN2 on the ULN2003 driver 1
#define motorPin3  4     // IN3 on the ULN2003 driver 1
#define motorPin4  5     // IN4 on the ULN2003 driver 1

#define motorPin5  8     // IN1 on the ULN2003 driver 2
#define motorPin6  9     // IN2 on the ULN2003 driver 2
#define motorPin7  10    // IN3 on the ULN2003 driver 2
#define motorPin8  11    // IN4 on the ULN2003 driver 2

// Initialize with pin sequence IN1-IN3-IN2-IN4 for using the AccelStepper with 28BYJ-48
AccelStepper stepper2(HALFSTEP, motorPin1, motorPin3, motorPin2, motorPin4);
AccelStepper stepper1(HALFSTEP, motorPin5, motorPin7, motorPin6, motorPin8);

int steps1=0; //number of steps for Left Stepper each loop
int steps2=0;
int rightBar = 0;
int leftBar = 0;
String input;
char command;
int dir = 1;

void setup() {
  
  stepper1.setMaxSpeed(2000.0);
  stepper2.setMaxSpeed(2000.0);

//  stepper1.setAcceleration(500.0);
 // stepper2.setAcceleration(500.0);
 
  stepper1.move(1);
  stepper2.move(-1);
  
  stepper1.setSpeed(100.0); //Set Stepper speed in RPM
  stepper2.setSpeed(100.0);
  
//  stepper1.setSpeed(1); //Set Stepper speed in RPM
//  stepper2.setSpeed(1);

  
  pinMode(LED_BUILTIN, OUTPUT);

  Serial.begin(9600); //Initialise commuication for Bluetooth
}

void loop() {

  if(stepper1.distanceToGo() == 0){
    stepper1.move(100 * dir);
    stepper1.setSpeed(rightBar * dir);
  }
  if(stepper2.distanceToGo() == 0){
    stepper2.move(100 * dir);
    stepper2.setSpeed(leftBar * dir);
  }

  input="";  
  digitalWrite(LED_BUILTIN, HIGH);
  while(Serial.available() > 0)
  {
    command = ((byte)Serial.read());
    if(command==':')
    {
      Serial.println("Ended command");
      digitalWrite(LED_BUILTIN, LOW);
      //Serial.println(input);
      break;
    }
    else {
      String temp = String(command);
      input += temp;
      //Serial.print(input);
    }
    delay(1);
  }
  if(input=="F")
  {
    dir = 1;
    Serial.println("Forward");
  }
  else if(input=="B")
  {
    dir = -1;
    Serial.println("Backward");
  }
  else if(input=="R0")
  {
    rightBar = 0;
    stepper1.stop();
    Serial.println(input);
  }
  else if(input=="R1")
  {
    rightBar = 100;
    Serial.println(input);
  }
  else if(input=="R2")
  {
    rightBar = 200;
    Serial.println(input);
  }
  else if(input=="R3")
  {
    rightBar = 300;
    Serial.println(input);
  }
  else if(input=="R4")
  {
    rightBar = 400;
    Serial.println(input);
  }
  else if(input=="R5")
  {
    rightBar = 500;
    Serial.println(input);
  }
  else if(input=="R6")
  {
    rightBar = 600;
    Serial.println(input);
  }
  else if(input=="R7")
  {
    rightBar = 700;
    Serial.println(input);
  }
  else if(input=="R8")
  {
    rightBar = 800;
    Serial.println(input);
  }
  else if(input=="R9")
  {
    rightBar = 900;
    Serial.println(input);
  }
  else if(input=="R10")
  {
    rightBar = 1000;
    Serial.println(input);
  }
  
  else if(input=="L0")
  {
    leftBar = 0;
    stepper2.stop();
    Serial.println(input);
  }
  else if(input=="L1")
  {
    leftBar = 100;
    Serial.println(input);
  }
  else if(input=="L2")
  {
    leftBar = 200;
    Serial.println(input);
  }
  else if(input=="L3")
  {
    leftBar = 300;
    Serial.println(input);
  }
  else if(input=="L4")
  {
    leftBar = 400;
    Serial.println(input);
  }
  else if(input=="L5")
  {
    leftBar = 500;
    Serial.println(input);
  }
  else if(input=="L6")
  {
    leftBar = 600;
    Serial.println(input);
  }
  else if(input=="L7")
  {
    leftBar = 700;
    Serial.println(input);
  }
  else if(input=="L8")
  {
    leftBar = 800;
    Serial.println(input);
  }
  else if(input=="L9")
  {
    leftBar = 900;
    Serial.println(input);
  }
  else if(input=="L10")
  {
    leftBar = 1000;
    Serial.println(input);
  }
  
  digitalWrite(LED_BUILTIN, LOW);
  stepper1.setSpeed(rightBar * dir);
  stepper2.setSpeed(leftBar * dir);
  stepper1.runSpeed();
  stepper2.runSpeed();
}
 
