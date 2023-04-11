# adhoc-ROS-Mobile

This is an ad-hoc version of the Android application, named ROS-Mobile, for the robot operating system (Rottmann et al, 2020). The ad-hoc app is developed to integrate the capabilities of MQTT clients and ROS nodes on a smartphone.

## Purpose

The purpose of this app is to tele-operate robotics manipulators using two joysticks: one of them is used for XY plane movements while the other one is for controlling the Z axis motion. At the same time, an IP camera can be displayed on the smartphone screen showing the images captured by eye-in-hand.
Moreover, the same space on the screen can show the virtual environment associated with the digital twin of the robot. The user can switch between both visualizations using two buttons. In addition, a new ROS subscriber serves as a traffic light (green, yellow and red) to indicates the forces on the end-effector, where there is an ultrasound camera to perform a medical scanning. Finally, another layout is used for displaying the images coming from the ultrasound camera, which are coming from a LAN/WAN socket (IP + port).

## Results

The results of this test will be shown on a paper submitted to the Robotics and Automation Letters. 
If the paper is accepted, more explanation will be written here.

## Activity for controlling the robotic manipulator

<p align="center">
    <img src="images/ad-hocRM.jpg" alt="Custom Master Chooser" width="200" />
<p/>

## References

Rottmann, N., Studt, N., Ernst, F., & Rueckert, E. (2020). Ros-mobile: An android application for the robot operating system. arXiv preprint arXiv:2011.02781.
