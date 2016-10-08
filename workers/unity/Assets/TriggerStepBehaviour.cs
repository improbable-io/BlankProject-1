using UnityEngine;
using System.Collections;
using Improbable.Collections;
using Improbable.Player;
using Improbable.Unity.Visualizer;

public class TriggerStepBehaviour : MonoBehaviour
{
    [Require] private PlayerControlsWriter playerControlsWriter;

    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Q))
        {
            // TODO (jonas): fill in expenditure here
            playerControlsWriter.Update.TriggerStep(new List<StepData.Expenditure>()).FinishAndSend();
        }
    }
}