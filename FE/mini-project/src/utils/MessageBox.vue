<script setup>
import { ElMessageBox, ElMessage } from 'element-plus'

const props = defineProps({
    title: {type: String, required: true},
    message: {type: String, required: true},
    type: { type: String, default: 'warning' },
    onConfirm: { type: Function, required: true }
});

const emit = defineEmits(['success','error']);

const execute = async () => {
    try {
    await ElMessageBox.confirm(
      props.message,
      props.title,
      {
        confirmButtonText: 'OK',
        cancelButtonText: 'Hủy',
        type: props.type,
      }
    );

    await props.onConfirm();
    ElMessage.success('Thành công');
    emit('success');
    } catch (error) {
        if (error !== 'cancel') {
            ElMessage.error('Thất bại');
            emit('error', error);
        }
    }
}


defineExpose({ execute });

</script>