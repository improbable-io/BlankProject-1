using UnityEngine;
using System.Collections;
using Improbable.Player;
using Improbable.Unity.Visualizer;

public class TriggerStepBehaviour : MonoBehaviour
{
    [Require] private StepWriter step;

    void Update()
    {
        if (Input.GetButtonDown(KeyCode.S.ToString()))
        {
            step.Update.TriggerTrigger().FinishAndSend();
        }
    }
}