using UnityEngine;
using System.Collections;
using Improbable.Player;
using Improbable.Unity.Visualizer;

public class TriggerStepBehaviour : MonoBehaviour
{
    [Require] private StepWriter step;

    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Q))
        {
            step.Update.TriggerTrigger().FinishAndSend();
        }
    }
}