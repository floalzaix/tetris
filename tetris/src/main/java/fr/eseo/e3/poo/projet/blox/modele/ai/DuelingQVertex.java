package fr.eseo.e3.poo.projet.blox.modele.ai;

import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.api.MaskState;
import org.deeplearning4j.nn.gradient.Gradient;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.graph.vertex.BaseGraphVertex;
import org.deeplearning4j.nn.graph.vertex.VertexIndices;
import org.deeplearning4j.nn.workspace.LayerWorkspaceMgr;
import org.nd4j.common.primitives.Pair;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;

public class DuelingQVertex extends BaseGraphVertex {
    //
    //  Constructeurs
    //
    protected DuelingQVertex(ComputationGraph graph, String name, int vertexIndex, VertexIndices[] inputVertices,
            VertexIndices[] outputVertices, DataType dataType) {
        super(graph, name, vertexIndex, inputVertices, outputVertices, dataType);
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean hasLayer() {
        return false;
    }

    @Override
    public Layer getLayer() {
        return null;
    }

    @Override
    public INDArray doForward(boolean training, LayerWorkspaceMgr workspaceMgr) {
        INDArray valeur = this.inputs[0];
        INDArray avantages = this.inputs[1];

        INDArray moyennes = avantages.mean(1).dup();

        return avantages.subColumnVector(moyennes).addColumnVector(valeur).detach(); // QValues
    }

    @Override
    public Pair<Gradient, INDArray[]> doBackward(boolean tbptt, LayerWorkspaceMgr workspaceMgr) {
        INDArray e = getEpsilon().detach();
        return new Pair<>(null, new INDArray[] {e, e});
    }

    @Override
    public void setBackpropGradientsViewArray(INDArray backpropGradientsViewArray) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Pair<INDArray, MaskState> feedForwardMaskArrays(INDArray[] maskArrays, MaskState currentMaskState, int minibatchSize) {
        throw new UnsupportedOperationException("Not supported.");
    }
    
}
